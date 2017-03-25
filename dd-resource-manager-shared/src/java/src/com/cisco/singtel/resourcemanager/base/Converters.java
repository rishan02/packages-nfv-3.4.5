package com.cisco.singtel.resourcemanager.base;

import com.tailf.conf.*;
import se.dataductus.common.nso.conf.ConfEmpty;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public class Converters {
    public interface ConfObjectConverter<T> {
        T convertFrom(ConfObject value);
        ConfValue convertTo(T value) throws ConfException;
        boolean exists(T value);
    }

    protected Converters() {
    }

    public static ConfObjectConverter<Boolean> confEmptyConverter = new ConfObjectConverter<Boolean>() {
        @Override
        public Boolean convertFrom(ConfObject value) {
            ConfEmpty actualValue = (ConfEmpty) value;
            return actualValue != null;
        }

        @Override
        public ConfValue convertTo(Boolean value) {
            return value != null && value.booleanValue() ? ConfEmpty.EMPTY_LEAF : null;
        }

        @Override
        public boolean exists(Boolean value) {
            return value != null && value.booleanValue();
        }
    };

    public static ConfObjectConverter<BigInteger> confUInt64Converter = new ConfObjectConverter<BigInteger>() {
        @Override
        public BigInteger convertFrom(ConfObject value) {
            ConfUInt64 actualValue = (ConfUInt64)value;
            return actualValue != null ? actualValue.bigIntegerValue() : null;
        }

        @Override
        public ConfValue convertTo(BigInteger value) {
            return value != null ? new ConfUInt64(value) : null;
        }

        @Override
        public boolean exists(BigInteger value) {
            return value != null;
        }
    };

    public static ConfObjectConverter<Long> confInt64Converter = new ConfObjectConverter<Long>() {
        @Override
        public Long convertFrom(ConfObject value) {
            ConfInt64 actualValue = (ConfInt64)value;
            return actualValue != null ? actualValue.longValue() : null;
        }

        @Override
        public ConfValue convertTo(Long value) {
            return value != null ? new ConfInt64(value) : null;
        }

        @Override
        public boolean exists(Long value) {
            return value != null;
        }
    };

    public static ConfObjectConverter<Integer> confInt32Converter = new ConfObjectConverter<Integer>() {
        @Override
        public Integer convertFrom(ConfObject value) {
            ConfInt32 actualValue = (ConfInt32)value;
            return actualValue != null ? (int)actualValue.intValue() : null;
        }

        @Override
        public ConfValue convertTo(Integer value) {
            return value != null ? new ConfInt32(value) : null;
        }

        @Override
        public boolean exists(Integer value) {
            return value != null;
        }
    };

    public static ConfObjectConverter<Long> confUInt32Converter = new ConfObjectConverter<Long>() {
        @Override
        public Long convertFrom(ConfObject value) {
            ConfUInt32 actualValue = (ConfUInt32)value;
            return actualValue != null ? actualValue.longValue() : null;
        }

        @Override
        public ConfValue convertTo(Long value) {
            return value != null ? new ConfUInt32(value) : null;
        }

        @Override
        public boolean exists(Long value) {
            return value != null;
        }
    };

    public static ConfObjectConverter<Integer> confUInt16Converter = new ConfObjectConverter<Integer>() {
        @Override
        public Integer convertFrom(ConfObject value) {
            ConfUInt16 actualValue = (ConfUInt16)value;
            return actualValue != null ? (int)actualValue.longValue() : null;
        }

        @Override
        public ConfValue convertTo(Integer value) {
            return value != null ? new ConfUInt16(value) : null;
        }

        @Override
        public boolean exists(Integer value) {
            return value != null;
        }
    };

    public static ConfObjectConverter<Short> confInt16Converter = new ConfObjectConverter<Short>() {
        @Override
        public Short convertFrom(ConfObject value) {
            ConfInt16 actualValue = (ConfInt16)value;
            return actualValue != null ? (short)actualValue.intValue() : null;
        }

        @Override
        public ConfValue convertTo(Short value) {
            return value != null ? new ConfInt16(value) : null;
        }

        @Override
        public boolean exists(Short value) {
            return value != null;
        }
    };

    public static ConfObjectConverter<Short> confUInt8Converter = new ConfObjectConverter<Short>() {
        @Override
        public Short convertFrom(ConfObject value) {
            ConfUInt8 actualValue = (ConfUInt8)value;
            return actualValue != null ? (short)actualValue.longValue() : null;
        }

        @Override
        public ConfValue convertTo(Short value) {
            return value != null ? new ConfUInt8(value) : null;
        }

        @Override
        public boolean exists(Short value) {
            return value != null;
        }
    };

    public static ConfObjectConverter<Byte> confInt8Converter = new ConfObjectConverter<Byte>() {
        @Override
        public Byte convertFrom(ConfObject value) {
            ConfInt16 actualValue = (ConfInt16) value;
            return actualValue != null ? (byte)actualValue.intValue() : null;
        }

        @Override
        public ConfValue convertTo(Byte value) {
            return value != null ? new ConfInt8(value) : null;
        }

        @Override
        public boolean exists(Byte value) {
            return value != null;
        }
    };

    public static ConfObjectConverter<String> confBufConverter = new ConfObjectConverter<String>() {
        @Override
        public String convertFrom(ConfObject value) {
            return value != null ? value.toString() : null;
        }

        @Override
        public ConfValue convertTo(String value) {
            return value != null ? new ConfBuf(value) : null;
        }

        @Override
        public boolean exists(String value) {
            return value != null;
        }
    };

    public static ConfObjectConverter<String> confBufOrEmptyConverter = new ConfObjectConverter<String>() {
        @Override
        public String convertFrom(ConfObject value) {
            return value != null ? value.toString() : "";
        }

        @Override
        public ConfValue convertTo(String value) {
            return value != null ? new ConfBuf(value) : null;
        }

        @Override
        public boolean exists(String value) {
            return value != null;
        }
    };

    public static ConfObjectConverter<Boolean> confBooleanConverter = new ConfObjectConverter<Boolean>() {
        @Override
        public Boolean convertFrom(ConfObject value) {
            ConfBool actualValue = (ConfBool) value;
            return actualValue != null ? actualValue.booleanValue() : null;
        }

        @Override
        public ConfValue convertTo(Boolean value) {
            return value != null ? new ConfBool(value) : null;
        }

        @Override
        public boolean exists(Boolean value) {
            return value != null;
        }
    };

    public static ConfObjectConverter<String> confIPv4Converter = new ConfObjectConverter<String>() {
        @Override
        public String convertFrom(ConfObject value) {
            return value != null ? value.toString() : null;
        }

        @Override
        public ConfValue convertTo(String value) throws ConfException {
            return value != null ? new ConfIPv4(value) : null;
        }

        @Override
        public boolean exists(String value) {
            return value != null;
        }
    };

    public static ConfObjectConverter<String> confIPv6Converter = new ConfObjectConverter<String>() {
        @Override
        public String convertFrom(ConfObject value) {
            return value != null ? value.toString() : null;
        }

        @Override
        public ConfValue convertTo(String value) throws ConfException {
            return value != null ? new ConfIPv6(value) : null;
        }

        @Override
        public boolean exists(String value) {
            return value != null;
        }
    };

    public static ConfObjectConverter<String> confIPv4PrefixConverter = new ConfObjectConverter<String>() {
        @Override
        public String convertFrom(ConfObject value) {
            return value != null ? value.toString() : null;
        }

        @Override
        public ConfValue convertTo(String value) throws ConfException {
            return value != null ? new ConfIPv4Prefix(value) : null;
        }

        @Override
        public boolean exists(String value) {
            return value != null;
        }
    };

    public static ConfObjectConverter<String> confIPv6PrefixConverter = new ConfObjectConverter<String>() {
        @Override
        public String convertFrom(ConfObject value) {
            return value != null ? value.toString() : null;
        }

        @Override
        public ConfValue convertTo(String value) throws ConfException {
            return value != null ? new ConfIPv6Prefix(value) : null;
        }

        @Override
        public boolean exists(String value) {
            return value != null;
        }
    };

    public static ConfObjectConverter<String> confIPv4AndPrefixLengthConverter = new ConfObjectConverter<String>() {
        @Override
        public String convertFrom(ConfObject value) {
            return value != null ? value.toString() : null;
        }

        @Override
        public ConfValue convertTo(String value) throws ConfException {
            return value != null ? new ConfIPv4AndPrefixLen(value) : null;
        }

        @Override
        public boolean exists(String value) {
            return value != null;
        }
    };

    public static ConfObjectConverter<String> confIPv6AndPrefixLengthConverter = new ConfObjectConverter<String>() {
        @Override
        public String convertFrom(ConfObject value) {
            return value != null ? value.toString() : null;
        }

        @Override
        public ConfValue convertTo(String value) throws ConfException {
            return value != null ? new ConfIPv6AndPrefixLen(value) : null;
        }

        @Override
        public boolean exists(String value) {
            return value != null;
        }
    };

    public static class ConfIdentityRefConverter<T> implements ConfObjectConverter<T> {
        private final Identities identities;
        private final Class<T> base;

        public ConfIdentityRefConverter(Identities identities, Class<T> base) {
            this.identities = identities;
            this.base = base;
        }

        @Override
        public T convertFrom(ConfObject value) {
            return value != null ? this.identities.getIdentity(value.toString(), this.base) : null;
        }

        @Override
        public ConfValue convertTo(T value) throws ConfException {
            return value != null ? new ConfIdentityRef(value.toString()) : null;
        }

        @Override
        public boolean exists(T value) {
            return value != null;
        }
    };

    public static class ConfListConverter<T> implements ConfObjectConverter<List<T>> {
        private final ConfObjectConverter<T> innerConverter;

        public ConfListConverter(ConfObjectConverter<T> innerConverter) {
            this.innerConverter = innerConverter;
        }

        @Override
        public List<T> convertFrom(ConfObject value) {
            if (value == null) {
                return null;
            }

            List<T> result = new ArrayList<>();
            ConfList actualValue = (ConfList)value;
            for (ConfObject elem : actualValue.elements()) {
                result.add(this.innerConverter.convertFrom(elem));
            }
            return result;
        }

        @Override
        public ConfValue convertTo(List<T> value) throws ConfException {
            if (value == null) {
                return null;
            }

            ConfObject[] array = new ConfObject[value.size()];
            for (int i = 0, size = value.size(); i < size; i++) {
                array[i] = this.innerConverter.convertTo(value.get(i));
            }

            return new ConfList(array);
        }

        @Override
        public boolean exists(List<T> value) {
            return value != null;
        }
    }

    public static class ConfValueConverter implements ConfObjectConverter<String> {
        private final String path;

        public ConfValueConverter(String path) {
            this.path = path;
        }

        @Override
        public String convertFrom(ConfObject value) {
            return value != null ? value.toString() : null;
        }

        @Override
        public ConfValue convertTo(String value) throws ConfException {
            return value != null ? ConfValue.getValueByString(this.path, value) : null;
        }

        @Override
        public boolean exists(String value) {
            return value != null;
        }
    };
}