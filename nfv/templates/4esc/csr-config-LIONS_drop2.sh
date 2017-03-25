platform console virtual
!
ip domain name nfv.connect.singtel.com
service password-encryption
!
enable password S1ngtel!
!         
aaa new-model
!         
aaa group server tacacs+ Singtel-TACACS server-private 203.101.215.250 key cisco server-private 202.160.202.200 key cisco ip vrf forwarding Mgmt ip tacacs source-interface GigabitEthernet1!aaa authentication login Singtel-AAA group Singtel-TACACS localaaa authentication enable default group Singtel-TACACS enableaaa authorization exec Singtel-AAA group Singtel-TACACS local if-authenticated aaa authorization commands 15 Singtel-AAA group Singtel-TACACS local aaa accounting send stop-record authentication failureaaa accounting exec Singtel-AAA start-stop group Singtel-TACACSaaa accounting commands 15 Singtel-AAA start-stop group Singtel-TACACS!
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
ip access-list extended ntp-peers permit ip host 203.208.155.236 any permit ip host 202.160.202.51 anyip access-list extended ntp-query-only deny   ip any any	
!
!
ip ssh source-interface GigabitEthernet1
ip ssh version 2
no ip ssh stricthostkeycheck
!
crypto key generate rsa modulus 1024 general-keys
!
logging alarm informationallogging trap debugginglogging source-interface GigabitEthernet1 vrf Mgmtlogging host 203.101.215.250 vrf Mgmtlogging host 202.160.202.206 vrf Mgmt!
snmp-server community Secret RO
!banner login ^C****************************************************** Access to this computer system is limited to      ** authorised users only.                            ** Unauthorised users may be subject to prosecution  ** under the Crimes Act or State legislation.        **                                                   ** Please note, ALL CUSTOMER DETAILS are confidential** and must not be disclosed.                        ******************************************************^C!         
line vty 0 15
 exec-timeout 30 0
 authorization commands 15 Singtel-AAA authorization exec Singtel-AAA accounting commands 15 Singtel-AAA accounting exec Singtel-AAA login authentication Singtel-AAA
 access-class 40 in vrf-also
 transport input telnet ssh
 transport output none
!
ntp source GigabitEthernet1ntp access-group peer ntp-peersntp access-group query-only ntp-query-onlyntp peer vrf Mgmt 202.160.202.51ntp peer vrf Mgmt 203.208.155.236 prefer!
end       
