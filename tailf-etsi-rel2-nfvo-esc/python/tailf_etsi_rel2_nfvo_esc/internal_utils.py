from struct import pack

import ncs
import ncs.maapi as maapi
import ncs.maagic as maagic

def is_ha_master_or_no_ha():
    with maapi.single_read_trans("", "system", db=ncs.OPERATIONAL) as trans:
        if trans.exists("/tfnm:ncs-state/tfnm:ha"):
            mode = str(maagic.get_node(trans, '/tfnm:ncs-state/tfnm:ha/tfnm:mode'))
            return (mode == 'master')
        else:
            return True

def is_ha_slave():
    with maapi.single_read_trans("", "system", db=ncs.OPERATIONAL) as trans:
        if trans.exists("/tfnm:ncs-state/tfnm:ha"):
            mode = str(maagic.get_node(trans,
                                       '/tfnm:ncs-state/tfnm:ha/tfnm:mode'))
            return (mode == 'slave' or mode == 'relay-slave')
        return False

def IPNetwork(network):
    addr = str(network).split('/')
    if ':' in network:
        return IPv6Network(address = addr[0], prefix_len = addr[1])
    else:
        return IPv4Network(address = addr[0], prefix_len = addr[1])

class IPv4Network:
    def __init__(self, address, prefix_len):
        IPV4LENGTH = 32
        self._ALL_ONES = pow(2, IPV4LENGTH) - 1
        self.version = 'ipv4'
        self.address = address
        self.subnet_mask = self._create_subnet_mask(prefix_len)

    def _create_subnet_mask(self, prefix_len_str):
        ipint = self._ALL_ONES ^ (self._ALL_ONES >> int(prefix_len_str))
        octets = []
        for _ in xrange(4):
            octets.insert(0, str(ipint & 0xFF))
            ipint >>= 8
        return '.'.join(octets)

class IPv6Network:
    def __init__(self, address, prefix_len):
        IPV6LENGTH = 128
        self._ALL_ONES = pow(2, IPV6LENGTH) - 1
        self.version = 'ipv6'
        self.address = address
        self.subnet_mask = self._create_subnet_mask(prefix_len)

    def _create_subnet_mask(self, prefix_len_str):
        ipint = self._ALL_ONES ^ (self._ALL_ONES >> int(prefix_len_str))
        hex_str = '%032x' % ipint
        hextets = []
        for x in range(0, 32, 4):
            hextets.append('%x' % int(hex_str[x:x+4], 16))
        return ':'.join(self._compress_hextets(hextets))

    def _compress_hextets(self, hextets):
        best_doublecolon_start = -1
        best_doublecolon_len = 0
        doublecolon_start = -1
        doublecolon_len = 0
        for index in range(len(hextets)):
            if hextets[index] == '0':
                doublecolon_len += 1
                if doublecolon_start == -1:
                    # Start of a sequence of zeros.
                    doublecolon_start = index
                if doublecolon_len > best_doublecolon_len:
                    # This is the longest sequence of zeros so far.
                    best_doublecolon_len = doublecolon_len
                    best_doublecolon_start = doublecolon_start
            else:
                doublecolon_len = 0
                doublecolon_start = -1

        if best_doublecolon_len > 1:
            best_doublecolon_end = (best_doublecolon_start +
                                    best_doublecolon_len)
            # For zeros at the end of the address.
            if best_doublecolon_end == len(hextets):
                hextets += ['']
            hextets[best_doublecolon_start:best_doublecolon_end] = ['']
            # For zeros at the beginning of the address.
            if best_doublecolon_start == 0:
                hextets = [''] + hextets
        return hextets

def is_notif_handling_required():
    with maapi.single_read_trans("", "system", db=ncs.RUNNING) as trans:
        if trans.exists("/nfvo-rel2:nfvo/nfvo-rel2-esc:settings-esc"):
            handling_required = str(trans.get_elem("/nfvo-rel2:nfvo/nfvo-rel2-esc:settings-esc/nfvo-rel2-esc:netconf-subscription/nfvo-rel2-esc:default-subscriber"))
            if 'true' == handling_required:
                return True
            else:
                return False
