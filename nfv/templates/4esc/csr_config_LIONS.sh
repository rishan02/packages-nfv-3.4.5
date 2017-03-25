platform console virtual
!
ip domain name nfv.connect.singtel.com
service password-encryption
!
enable password S1ngtel!
!         
aaa new-model
!         
username Admin privilege 15 password S1ngtel!
!    
vrf definition Mgmt
!
address-family ipv4
exit-address-family
!     
!         
interface GigabitEthernet1
 description management
 vrf forwarding Mgmt
 ip address ${NICID_0_IP_ADDRESS} ${NICID_0_NETMASK}
 no shut
!         
interface GigabitEthernet2
  no shut
!         
interface GigabitEthernet3
 no shut
!         
!         
virtual-service csr_mgmt
!         
!         
ip route vrf Mgmt 0.0.0.0 0.0.0.0 172.17.11.1
!    
access-list 40 permit 172.17.11.0 0.0.0.255	
!
!
ip ssh source-interface GigabitEthernet1
ip ssh version 2
no ip ssh stricthostkeycheck
!
crypto key generate rsa modulus 1024 general-keys
!
snmp-server community Secret RO
!         
line vty 0 15
 exec-timeout 30 0
 access-class 40 in vrf-also
 transport input telnet ssh
 transport output none
end       
