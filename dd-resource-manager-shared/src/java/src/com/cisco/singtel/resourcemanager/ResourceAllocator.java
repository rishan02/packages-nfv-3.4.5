package com.cisco.singtel.resourcemanager;

import com.cisco.singtel.resourcemanager.base.Scope;
import com.cisco.singtel.resourcemanager.exceptions.IdPoolExhaustedException;
import com.cisco.singtel.resourcemanager.exceptions.IpPoolExhaustedException;
import com.cisco.singtel.resourcemanager.exceptions.ResourceAllocationException;
import com.cisco.singtel.resourcemanager.exceptions.ResourceAllocationNoFreeObjectException;
import com.cisco.singtel.resourcemanager.model.SingtelRm;
import com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.HostIdPool;
import com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.IpPool;
import com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.IpPoolList;
import com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.ObjectPool;
import com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.ObjectPoolList;
import com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.IdPool;
import com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.IdPoolList;
import com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.objectpool.Objects;
import com.cisco.singtel.resourcemanager.util.ResourceName;
import com.google.common.net.InetAddresses;
import com.tailf.conf.*;
import com.tailf.maapi.Maapi;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.log4j.Logger;
import se.dataductus.common.nso.channel.MaapiChannel;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.TreeSet;

import static java.lang.String.format;

/**
  * ResourceAllocator class to allocate resources
  */
public class ResourceAllocator {
    private static final Logger LOGGER = Logger.getLogger(ResourceAllocator.class);
    private static final Integer HOST_ID_MIN = 1;
    private static final Integer HOST_ID_MAX = 4000;
    private static ResourceAllocator instance = null;

    private ResourceAllocator() {
    }

    /**
     * Singleton to make sure we only have one instance of this class.
     *
     * @return instance
     */
    public static synchronized ResourceAllocator getInstance() {
        if (instance == null) {
            instance = new ResourceAllocator();
        }
        return instance;
    }

    /**
     * Used by services to get allocated vlan ID.
     *
     * @param maapi        Maapi to use.
     * @param th           Transaction ID.
     * @param siteCode     Site code
     * @param podName      Pod name
     * @param dciName      DCI name
     * @param dciLinkEndPoint      DCI link end-point
     * @param serviceName  Service name
     * @param ingress Boolean, true for ingress, false for egress
     * @return Allocated ID
     * @throws ResourceAllocationException On failure
     */
    public synchronized Integer getAllocatedVlanId(Maapi maapi,
                                                   int th,
                                                   String siteCode,
                                                   String podName,
                                                   String dciName,
                                                   String dciLinkEndPoint,
                                                   String serviceName,
                                                   boolean ingress)
            throws ResourceAllocationException {

        MaapiChannel maapiChannel = new MaapiChannel(maapi, th);    // No need to auto-close since nothing should happen with external Maapi
        String poolName = ResourceName.create(siteCode, podName, dciName, dciLinkEndPoint);
        String consumer = ResourceName.create(serviceName, ingress ? "ingress" : "egress");
        return this.getAllocatedId(maapiChannel, poolName, consumer);
    }

    /**
     * Used by services to get allocated management IP.
     *
     * @param maapi        Maapi to use.
     * @param th           Transaction ID.
     * @param siteCode     Site code
     * @param podName      Pod name
     * @param serviceName  Service name
     * @return Allocated management IP
     * @throws ResourceAllocationException On failure
     */
    public synchronized String getAllocatedManagementIp(Maapi maapi,
                                                        int th,
                                                        String siteCode,
                                                        String podName,
                                                        String serviceName)
            throws ResourceAllocationException {
        MaapiChannel maapiChannel = new MaapiChannel(maapi, th);    // No need to auto-close since nothing should happen with external Maapi
        String poolName = ResourceName.create("VCPE",siteCode, podName);
        String consumer = ResourceName.create(serviceName);
        return this.getAllocatedIp(maapiChannel, poolName, consumer);
    }


    /**
     * Called by pods-service to create a new or modify an existing ID pool.
     *
     * @param maapiChannel Maapi channel to use
     * @param poolName     Pool name
     * @param ranges       ID ranges
     */
    public synchronized void createOrModifyIdPool(MaapiChannel maapiChannel, String poolName, List<Pair<Integer, Integer>> ranges) throws ResourceAllocationException {
        IdPoolList idPools = new SingtelRm().resourceManager().pools().idPool();
        IdPool pool = idPools.elem(poolName);

        if (pool._exists(maapiChannel)) {
            pool = pool._read(maapiChannel, Scope.ALL);
            if (pool.allAllocated().exists()) {
                for (Integer allocated : pool.allAllocated().get()) {
                    boolean ok = false;
                    for (Pair<Integer, Integer> range : ranges) {
                        if (range.getLeft() <= allocated && allocated <= range.getRight()) {
                            ok = true;
                            break;
                        }
                    }

                    if (!ok) {
                        throw new ResourceAllocationException(format("Can not modify pool %s. New range(s) does not include previous allocation(s).", poolName));
                    }
                }
            }

            for (com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.idpool.Ranges range : pool.ranges()) {
                maapiChannel.delete(range._getPath());
            }
        } else {
            maapiChannel.create(pool._getPath());
        }

        for (Pair<Integer, Integer> range : ranges) {
            maapiChannel.create(pool.ranges().elem(range.getLeft(), range.getRight())._getPath());
        }
    }

    /**
     * Delete existing ID pool.
     * @param maapiChannel Maapi channel to use
     * @param poolName     Pool name
     * @throws ResourceAllocationException If pool has active allocations
     */
    public synchronized void deleteIdPool(MaapiChannel maapiChannel, String poolName) throws ResourceAllocationException {
        IdPool pool = new SingtelRm().resourceManager().pools().idPool().elem(poolName);

        if (pool._exists(maapiChannel)) {
            pool = pool._read(maapiChannel, Scope.LEAVES);
            if (pool.allAllocated().exists() && !pool.allAllocated().get().isEmpty()) {
                throw new ResourceAllocationException(format("Can not delete pool %s as it has active allocation(s).", poolName));
            }

            maapiChannel.delete(pool._getPath());
        }
    }

    /**
     * Called by RPC to allocate ID for 'consumer'.
     *
     * @param maapiChannel Maapi channel to use
     * @param poolName     Pool name
     * @param consumer     Unique consumer key
     * @throws ResourceAllocationException On failure
     */
    public synchronized void allocateId(MaapiChannel maapiChannel, String poolName, String consumer) throws ResourceAllocationException {
        this.allocateId(maapiChannel, poolName, consumer, null);
    }

    /**
     * Called by RPC to allocate ID for 'consumer'.
     *
     * @param maapiChannel Maapi channel to use
     * @param poolName     Pool name
     * @param consumer     Unique consumer key
     * @param specificId   ID to allocate
     * @throws ResourceAllocationException On failure
     */
    public synchronized void allocateId(MaapiChannel maapiChannel, String poolName, String consumer, Integer specificId) throws ResourceAllocationException {
        IdPoolList idPools = new SingtelRm().resourceManager().pools().idPool();
        if (!idPools.elem(poolName)._exists(maapiChannel)) {
            throw new ResourceAllocationException(format("Pool %s does not exist", poolName));
        }

        IdPool pool = idPools.elem(poolName)._read(maapiChannel, Scope.ALL);
        if (pool.allocatedByConsumer().elem(consumer)._exists()) {
            throw new ResourceAllocationException(format("Consumer %s already has an allocated ID", consumer));
        }

        TreeSet<Integer> all = new TreeSet<>();

        for (com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.idpool.Ranges range : pool.ranges()) {
            for (int i = range.min().get(); i <= range.max().get(); i++) {
                all.add(i);
            }
        }

        Integer lastAllocated = pool.lastAllocated().get();
        Integer toAllocate = null;

        if (specificId != null) {
            if ((!pool.allAllocated().exists() || !pool.allAllocated().get().contains(specificId)) && all.contains(specificId)) {
                toAllocate = specificId;
            } else {
                throw new ResourceAllocationException(format("Specified ID %d is not available", specificId));
            }
        } else {
            for (Integer id : all) {
                if (!pool.allAllocated().exists() || !pool.allAllocated().get().contains(id)) {
                    if (lastAllocated == null || id > lastAllocated) {
                        toAllocate = id;
                        break;
                    }
                    if (toAllocate == null) {
                        toAllocate = id;
                    }
                }
            }
        }

        if (toAllocate == null) {
            throw new IdPoolExhaustedException("No free ID's");
        }

        ConfUInt16 allocated = new ConfUInt16(toAllocate);
        List<Integer> allAllocated = new ArrayList<>();
        if (pool.allAllocated().exists()) {
            allAllocated.addAll(pool.allAllocated().get());
        }
        allAllocated.add(toAllocate);

        pool.allocatedByConsumer().elem(consumer).allocated().set(allocated);
        pool.lastAllocated().set(allocated);
        pool.allAllocated().set(toIdLeafList(allAllocated));

        try {
            pool._write(maapiChannel);
        } catch (ConfException e) {
            LOGGER.error("Pool write exception", e);
            throw new ResourceAllocationException("Unknown error");
        }
    }

    /**
     * Used by services to get allocated ID.
     *
     * @param maapiChannel Maapi channel to use.
     * @param poolName     Pool name
     * @param consumer     Unique consumer key
     * @return Allocated ID
     * @throws ResourceAllocationException On failure
     */
    public synchronized Integer getAllocatedId(MaapiChannel maapiChannel, String poolName, String consumer) throws ResourceAllocationException {
        IdPoolList idPools = new SingtelRm().resourceManager().pools().idPool();
        if (!idPools.elem(poolName)._exists(maapiChannel)) {
            throw new ResourceAllocationException(format("Pool %s does not exist", poolName));
        }

        IdPool pool = idPools.elem(poolName)._read(maapiChannel, Scope.ALL);
        if (!pool.allocatedByConsumer().elem(consumer)._exists()) {
            throw new ResourceAllocationException(format("Consumer %s does not have an allocated ID", consumer));
        }

        return pool.allocatedByConsumer().elem(consumer).allocated().get();
    }

    /**
     * Called by RPC to deallocate ID for 'consumer'.
     *
     * @param maapiChannel Maapi channel to use
     * @param poolName     Pool name
     * @param consumer     Unique consumer key
     * @throws ResourceAllocationException On failure
     */
    public synchronized void deallocateId(MaapiChannel maapiChannel, String poolName, String consumer) throws ResourceAllocationException {
        IdPoolList idPools = new SingtelRm().resourceManager().pools().idPool();
        if (!idPools.elem(poolName)._exists(maapiChannel)) {
            throw new ResourceAllocationException(format("Pool %s does not exist", poolName));
        }

        IdPool pool = idPools.elem(poolName)._read(maapiChannel, Scope.ALL);

        if (pool.allocatedByConsumer().elem(consumer)._exists()) {
            Integer allocated = pool.allocatedByConsumer().elem(consumer).allocated().get();
            List<Integer> allAllocated = pool.allAllocated().get();
            allAllocated.remove(allocated);
            pool.allAllocated().set(allAllocated);
            pool.allocatedByConsumer().elem(consumer)._delete();
        }

        try {
            pool._write(maapiChannel);
        } catch (ConfException e) {
            LOGGER.error("Pool write exception", e);
            throw new ResourceAllocationException("Unknown error");
        }
    }

    /**
     * Allocate host ID
     * @param maapiChannel Maapi channel to use
     * @return Unique host ID
     * @throws ResourceAllocationException On failure
     */
    public synchronized Integer allocateHostId(MaapiChannel maapiChannel) throws ResourceAllocationException {
        HostIdPool pool = new SingtelRm().resourceManager().pools().hostIdPool()._read(maapiChannel, Scope.ALL);

        Integer lastAllocated = pool.lastAllocated().get() == null ? HOST_ID_MIN - 1 : pool.lastAllocated().get();
        Integer toAllocate = lastAllocated.equals(HOST_ID_MAX) ? HOST_ID_MIN : lastAllocated + 1;
        boolean allocate = false;

        while (!toAllocate.equals(lastAllocated)) {
            if (!pool.allAllocated().exists() || !pool.allAllocated().get().contains(toAllocate)) {
                allocate = true;
                break;
            }

            toAllocate = toAllocate.equals(HOST_ID_MAX) ? HOST_ID_MIN : toAllocate + 1;
        }

        if (!allocate) {
            throw new IdPoolExhaustedException("No free ID's");
        }

        ConfUInt16 allocated = new ConfUInt16(toAllocate);
        List<Integer> allAllocated = new ArrayList<>();
        if (pool.allAllocated().exists()) {
            allAllocated.addAll(pool.allAllocated().get());
        }
        allAllocated.add(toAllocate);

        pool.lastAllocated().set(allocated);
        pool.allAllocated().set(toIdLeafList(allAllocated));

        try {
            pool._write(maapiChannel);
        } catch (ConfException e) {
            LOGGER.error("Pool write exception", e);
            throw new ResourceAllocationException("Unknown error");
        }

        return toAllocate;
    }

    /**
     * Deallocate host ID
     * @param maapiChannel Maapi channel to use
     * @param id Id to deallocate
     */
    public synchronized void deallocateHostId(MaapiChannel maapiChannel, Integer id) {
        HostIdPool pool = new SingtelRm().resourceManager().pools().hostIdPool()._read(maapiChannel, Scope.ALL);

        List<Integer> allAllocated = pool.allAllocated().get();
        allAllocated.remove(id);
        pool.allAllocated().set(allAllocated);

        try {
            pool._write(maapiChannel);
        } catch (ConfException e) {
            LOGGER.error("Pool write exception", e);
            throw new ResourceAllocationException("Unknown error");
        }
    }

    /**
     * Called by pods-service to create a new or modify an existing IP pool.
     *
     * @param maapiChannel Maapi channel to use
     * @param poolName     Pool name
     * @param ranges       IP ranges (a list with pairs of start and end IP addresses)
     */
    public synchronized void createOrModifyIpPool(MaapiChannel maapiChannel, String poolName, List<Pair<String, String>> ranges) throws ResourceAllocationException {
        IpPoolList ipPools = new SingtelRm().resourceManager().pools().ipPool();
        IpPool pool = ipPools.elem(poolName);

        if (maapiChannel.exists(pool._getPath())) {
            pool = pool._read(maapiChannel, Scope.ALL);
            if (pool.allAllocated().get() != null) {
                for (String allocatedString : pool.allAllocated().get()) {
                    int allocated = InetAddresses.coerceToInteger(InetAddresses.forString(allocatedString));
                    boolean inRange = false;
                    for (Pair<String, String> range : ranges) {
                        if (InetAddresses.coerceToInteger(InetAddresses.forString(range.getLeft())) <= allocated &&
                                allocated <= InetAddresses.coerceToInteger(InetAddresses.forString(range.getRight()))) {
                            inRange = true;
                            break;
                        }
                    }

                    if (!inRange) {
                        throw new ResourceAllocationException(format("Can not modify pool %s. New range(s) does not include previous allocation(s).", poolName));
                    }
                }
            }

            for (com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.ippool.Ranges range : pool.ranges()) {
                maapiChannel.delete(range._getPath());
            }
        } else {
            maapiChannel.create(pool._getPath());
        }

        for (Pair<String, String> range : ranges) {
            maapiChannel.create(pool.ranges().elem(range.getLeft(), range.getRight())._getPath());
        }
    }

    /**
     * Delete existing IP pool.
     * @param maapiChannel Maapi channel to use
     * @param poolName     Pool name
     * @throws ResourceAllocationException If pool has active allocations
     */
    public synchronized void deleteIpPool(MaapiChannel maapiChannel, String poolName) throws ResourceAllocationException {
        IpPool pool = new SingtelRm().resourceManager().pools().ipPool().elem(poolName);

        if (pool._exists(maapiChannel)) {
            pool = pool._read(maapiChannel, Scope.LEAVES);
            if (pool.allAllocated().exists() && !pool.allAllocated().get().isEmpty()) {
                throw new ResourceAllocationException(format("Can not delete pool %s as it has active allocation(s).", poolName));
            }

            maapiChannel.delete(pool._getPath());
        }
    }

    /**
     * Called by RPC to allocate IP for 'consumer'.
     *
     * @param maapiChannel Maapi channel to use.
     * @param poolName     Pool name
     * @param consumer     Unique consumer key
     * @throws ResourceAllocationException On failure
     */
    public synchronized void allocateIp(MaapiChannel maapiChannel, String poolName, String consumer) throws ResourceAllocationException {
        IpPoolList ipPools = new SingtelRm().resourceManager().pools().ipPool();
        if (!ipPools.elem(poolName)._exists(maapiChannel)) {
            throw new ResourceAllocationException(format("Pool %s does not exist", poolName));
        }

        IpPool pool = ipPools.elem(poolName)._read(maapiChannel, Scope.ALL);

        if (pool.allocatedByConsumer().elem(consumer)._exists()) {
            throw new ResourceAllocationException(format("Consumer %s already has an allocated IP address", consumer));
        }

        TreeSet<Integer> all = new TreeSet<>();

        for (com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.ippool.Ranges ranges : pool.ranges()) {
            int current = InetAddresses.coerceToInteger(InetAddresses.forString(ranges.min().get()));
            int stop = InetAddresses.coerceToInteger(InetAddresses.forString(ranges.max().get()));
            for (; current <= stop; current++) {
                all.add(current);
            }
        }

        InetAddress lastAllocated = !pool.lastAllocated().exists() ? null : InetAddresses.forString(pool.lastAllocated().get());
        InetAddress toAllocate = null;

        for (Integer ip : all) {
            if (!pool.allAllocated().exists() || !pool.allAllocated().get().contains(InetAddresses.toAddrString(InetAddresses.fromInteger(ip)))) {
                if (lastAllocated == null || ip > InetAddresses.coerceToInteger(lastAllocated)) {
                    toAllocate = InetAddresses.fromInteger(ip);
                    break;
                }
                if (toAllocate == null) {
                    toAllocate = InetAddresses.fromInteger(ip);
                }
            }
        }

        if (toAllocate == null) {
            throw new IpPoolExhaustedException("No free IP addresses");
        }

        ConfIPv4 allocated = new ConfIPv4(toAllocate);
        List<String> allAllocated = new ArrayList<>();
        if (pool.allAllocated().exists()) {
            allAllocated.addAll(pool.allAllocated().get());
        }

        allAllocated.add(InetAddresses.toAddrString(toAllocate));

        pool.allocatedByConsumer().elem(consumer).allocated().set(allocated);
        pool.lastAllocated().set(allocated);
        pool.allAllocated().set(toIpLeafList(allAllocated));

        try {
            pool._write(maapiChannel);
        } catch (ConfException e) {
            LOGGER.error("Pool write exception", e);
            throw new ResourceAllocationException("Unknown error");
        }
    }
    /**
     * Used by services to get allocated IP.
     *
     * @param maapiChannel Maapi channel to use.
     * @param poolName     Pool name
     * @param consumer     Unique consumer key
     * @return Allocated IP address
     * @throws ResourceAllocationException On failure
     */
    public synchronized String getAllocatedIp(MaapiChannel maapiChannel, String poolName, String consumer) throws ResourceAllocationException {
        IpPoolList ipPools = new SingtelRm().resourceManager().pools().ipPool();
        if (!ipPools.elem(poolName)._exists(maapiChannel)) {
            throw new ResourceAllocationException(format("Pool %s does not exist", poolName));
        }

        IpPool pool = ipPools.elem(poolName)._read(maapiChannel, Scope.ALL);

        if (!pool.allocatedByConsumer().elem(consumer)._exists()) {
            throw new ResourceAllocationException(format("Consumer %s does not have an allocated IP address", consumer));
        }

        return pool.allocatedByConsumer().elem(consumer).allocated().get();
    }

    /**
     * Called by RPC to deallocate IP for 'consumer'.
     *
     * @param maapiChannel Maapi channel to use
     * @param poolName     Pool name
     * @param consumer     Unique consumer key
     * @throws ResourceAllocationException On failure
     */
    public synchronized void deallocateIp(MaapiChannel maapiChannel, String poolName, String consumer) throws ResourceAllocationException {
        IpPoolList ipPools = new SingtelRm().resourceManager().pools().ipPool();
        if (!ipPools.elem(poolName)._exists(maapiChannel)) {
            throw new ResourceAllocationException(format("Pool %s does not exist", poolName));
        }

        IpPool pool = ipPools.elem(poolName)._read(maapiChannel, Scope.ALL);

        if (pool.allocatedByConsumer().elem(consumer)._exists()) {
            String allocated = pool.allocatedByConsumer().elem(consumer).allocated().get();
            List<String> allAllocated = pool.allAllocated().get();
            allAllocated.remove(allocated);
            pool.allAllocated().set(allAllocated);
            pool.allocatedByConsumer().elem(consumer)._delete();
        }

        try {
            pool._write(maapiChannel);
        } catch (ConfException e) {
            LOGGER.error("Pool write exception", e);
            throw new ResourceAllocationException("Unknown error");
        }
    }

    /**
     * Called by service to create a Object pool.
     *
     * @param maapiChannel Maapi channel to use
     * @param poolName     Pool name
     */
    public synchronized void createObjectPool(MaapiChannel maapiChannel, String poolName) throws ResourceAllocationException {
        ObjectPoolList objectPools = new SingtelRm().resourceManager().pools().objectPool();
        ObjectPool pool = objectPools.elem(poolName);

        if (!pool._exists(maapiChannel)) {
            maapiChannel.create(pool._getPath());
        }
    }

    /**
     * Called by service to add or update a Object in the pool.
     *
     * @param maapiChannel Maapi channel to use
     * @param poolName     Pool to add object to
     * @param objectName   Object name
     * @param capacity     Capacity of the object
     * @param profile      Profile of object if needed
     * @param bandwidth    Bandwidth (low and high)
     */
    public synchronized void addOrUpdateObject(MaapiChannel maapiChannel, String poolName, String objectName, long capacity, String profile, Pair<Long, Long> bandwidth)
            throws ResourceAllocationException {

        ObjectPoolList objectPools = new SingtelRm().resourceManager().pools().objectPool();
        ObjectPool pool = objectPools.elem(poolName);

        if (!pool._exists(maapiChannel)) {
            throw new ResourceAllocationException(format("Pool %s does not exist.", poolName));
        }
        pool = pool._read(maapiChannel, Scope.ALL);
        Objects newObject = pool.objects().elem(objectName);

        if (newObject._exists()) {
            if (newObject.consumers()._exists() && newObject.consumers().get().size() > capacity) {
                throw new ResourceAllocationException(format("Can not modify object %s in pool %s. Capacity is too small.", objectName, poolName));
            }
        } else {
            newObject._create();
        }

        newObject.capacity().set(capacity);
        newObject.profile().set(profile);
        if (bandwidth != null) {
            newObject.bandwidth().low().set(bandwidth.getLeft());
            newObject.bandwidth().high().set(bandwidth.getRight());
        }

        try {
            pool._write(maapiChannel);
        } catch (ConfException e) {
            LOGGER.error("Pool write exception", e);
            throw new ResourceAllocationException("Unknown error");
        }
    }

    /**
     * Called by service to delete an Object in the pool.
     * Will remove the pool together with the last object.
     *
     * @param maapiChannel Maapi channel to use
     * @param poolName     Pool to delete object from
     * @param objectName   Object name
     */
    public synchronized void deleteObject(MaapiChannel maapiChannel, String poolName, String objectName) throws ResourceAllocationException {
        ObjectPoolList objectPools = new SingtelRm().resourceManager().pools().objectPool();
        ObjectPool pool = objectPools.elem(poolName);

        if (!pool._exists(maapiChannel)) {
            throw new ResourceAllocationException(format("Pool %s does not exist.", poolName));
        }
        pool = pool._read(maapiChannel, Scope.ALL);

        if (pool.objects().elem(objectName)._exists()) {
            pool.objects().elem(objectName)._delete();
        } else {
            LOGGER.debug(format("Tried to remove %s object, which was not in the pool", objectName));
        }

        try {
            pool._write(maapiChannel);
        } catch (ConfException e) {
            LOGGER.error("Pool write exception", e);
            throw new ResourceAllocationException("Unknown error");
        }
    }

    /**
     * Used by RPC to allocate objects (vdoms).
     *
     * @param maapiChannel Maapi channel to use.
     * @param poolName     Pool name
     * @param consumer     Unique consumer key
     * @return Object allocated to the consumer
     * @throws ResourceAllocationException On failure
     */
    public synchronized String allocateObject(MaapiChannel maapiChannel, String poolName, Long bandwidth, String consumer)
            throws IllegalArgumentException, ResourceAllocationException {

        ObjectPoolList objectPools = new SingtelRm().resourceManager().pools().objectPool();
        if (!objectPools.elem(poolName)._exists(maapiChannel)) {
            throw new ResourceAllocationException(format("Pool %s does not exist.", poolName));
        }

        if (bandwidth == null) {
            throw new IllegalArgumentException("Bandwidth can't be null");
        }

        ObjectPool pool = objectPools.elem(poolName)._read(maapiChannel, Scope.ALL);

        for (Objects object : pool.objects()) {
            if (isAvailable(object, bandwidth, object.consumers().get())) {
                String objectName = object.object().get();
                setObject(maapiChannel, poolName, objectName, consumer);
                return objectName;
            }
        }

        throw new ResourceAllocationNoFreeObjectException(format("No free objects in pool %s for consumer %s.", poolName, consumer));
    }

    private boolean isAvailable(Objects object, Long bandwidth, List<String> consumers) {
        return object.capacity().get() != null && (consumers == null || consumers.size() < object.capacity().get())
                && objectBandwidthLimitCheck(object, bandwidth);
    }

    private boolean objectBandwidthLimitCheck(Objects object, Long bandwidth) {
        Long lowBw = object.bandwidth().low().get();
        lowBw = lowBw == null ? 0L : lowBw;

        Long highBw = object.bandwidth().high().get();
        highBw = highBw == null ? Long.MAX_VALUE : highBw;

        return lowBw <= bandwidth && bandwidth <= highBw;
    }

    /**
     * Used by RPC to check bandwidth limits on object.
     *
     * @param maapiChannel Maapi channel to use.
     * @param poolName     Pool name
     * @param objectName   Object name
     * @param bandwidth    Target bandwidth
     * @return True if bandwidth is within object's limits.
     * @throws ResourceAllocationException On failure
     */
    public synchronized boolean objectBandwidthLimitCheck(MaapiChannel maapiChannel, String poolName, String objectName, Long bandwidth) {
        ObjectPoolList objectPools = new SingtelRm().resourceManager().pools().objectPool();
        if (!objectPools.elem(poolName)._exists(maapiChannel)) {
            throw new ResourceAllocationException(format("Pool %s does not exist.", poolName));
        }

        ObjectPool pool = objectPools.elem(poolName);

        if (!pool.objects().elem(objectName)._exists(maapiChannel)) {
            throw new ResourceAllocationException(format("Object %s does not exist in pool %s.", objectName, poolName));
        }

        if (bandwidth == null) {
            throw new IllegalArgumentException("Bandwidth can't be null");
        }

        Objects object = pool.objects().elem(objectName)._read(maapiChannel, Scope.ALL);

        return objectBandwidthLimitCheck(object, bandwidth);
    }

    /**
     * Used by services to get the consumers object.
     *
     * @param maapiChannel Maapi channel to use.
     * @param poolName     Pool name
     * @param consumer     Unique consumer key
     * @return Object allocated to the consumer
     * @throws ResourceAllocationException On failure
     */
    public synchronized String getObject(MaapiChannel maapiChannel, String poolName, String consumer) throws ResourceAllocationException {
        ObjectPoolList objectPools = new SingtelRm().resourceManager().pools().objectPool();
        if (!objectPools.elem(poolName)._exists(maapiChannel)) {
            throw new ResourceAllocationException(format("Pool %s does not exist.", poolName));
        }

        ObjectPool pool = objectPools.elem(poolName)._read(maapiChannel, Scope.ALL);

        if (!pool.allocatedByConsumer().elem(consumer)._exists()) {
            throw new ResourceAllocationException(format("Consumer %s does not have an allocated object.", consumer));
        }

        return pool.allocatedByConsumer().elem(consumer).allocated().get();
    }

    /**
     * Used by services to map a consumer and a object.
     *
     * @param maapiChannel Maapi channel to use.
     * @param poolName     Pool name
     * @param objectName   Object to allocate to consumer
     * @param consumer     Unique consumer key to map to object
     * @throws ResourceAllocationException On failure
     */
    public synchronized void setObject(MaapiChannel maapiChannel, String poolName, String objectName, String consumer) throws ResourceAllocationException {
        ObjectPoolList objectPools = new SingtelRm().resourceManager().pools().objectPool();
        ObjectPool pool = objectPools.elem(poolName);

        if (!pool._exists(maapiChannel)) {
            throw new ResourceAllocationException(format("Pool %s does not exist.", poolName));
        }
        pool = pool._read(maapiChannel, Scope.ALL);

        if (!pool.objects().elem(objectName)._exists()) {
            throw new ResourceAllocationException(format("Object %s does not exist.", objectName));
        }
        Objects object = pool.objects().elem(objectName);

        if (!object.consumers()._exists()) {
            object.consumers().set(Collections.emptyList());
        }

        if (object.capacity().get() <= object.consumers().get().size()) {
            throw new ResourceAllocationNoFreeObjectException(format("Can not map consumer %s and object %s in pool %s. Objects capacity is too small.", consumer, objectName, poolName));
        }

        List<String> consumerList = new ArrayList<>(object.consumers().get());
        if (!consumerList.contains(consumer)) {
            consumerList.add(consumer);
            object.consumers().set(consumerList);
        }

        if (!pool.allocatedByConsumer().elem(consumer)._exists()) {
            pool.allocatedByConsumer().elem(consumer)._create();
        }
        pool.allocatedByConsumer().elem(consumer).allocated().set(objectName);

        try {
            pool._write(maapiChannel);
        } catch (ConfException e) {
            LOGGER.error("Pool write exception", e);
            throw new ResourceAllocationException("Unknown error");
        }
    }

    /**
     * Called by RPC to unmap a consumer from an object.
     *
     * @param maapiChannel Maapi channel to use
     * @param poolName     Pool name
     * @param consumer     Unique consumer key
     * @throws ResourceAllocationException On failure
     */
    public synchronized void deallocateObject(MaapiChannel maapiChannel, String poolName, String consumer) throws ResourceAllocationException {
        ObjectPoolList objectPools = new SingtelRm().resourceManager().pools().objectPool();
        ObjectPool pool = objectPools.elem(poolName);

        if (!pool._exists(maapiChannel)) {
            throw new ResourceAllocationException(format("Pool %s does not exist.", poolName));
        }
        pool = pool._read(maapiChannel, Scope.ALL);

        if (pool.allocatedByConsumer().elem(consumer)._exists()) {
            String objectName = pool.allocatedByConsumer().elem(consumer).allocated().get();
            Objects object = pool.objects().elem(objectName);
            List<String> consumerList = object.consumers().get();
            consumerList.remove(consumer);
            object.consumers().set(consumerList);
            pool.allocatedByConsumer().elem(consumer)._delete();
        }

        try {
            pool._write(maapiChannel);
        } catch (ConfException e) {
            LOGGER.error("Pool write exception", e);
            throw new ResourceAllocationException("Unknown error");
        }
    }

    private ConfList toIdLeafList(List<Integer> ids) {
        ConfList result = new ConfList();

        for (Integer id : ids) {
            result.addElem(new ConfUInt16(id));
        }

        return result;
    }

    private ConfList toIpLeafList(List<String> ips) {
        ConfList result = new ConfList();

        for (String ip : ips) {
            result.addElem(new ConfIPv4(InetAddresses.forString(ip)));
        }

        return result;
    }
    
}
