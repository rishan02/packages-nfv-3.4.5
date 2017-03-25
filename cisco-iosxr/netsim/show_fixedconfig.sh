#!/bin/bash

cat <<EOF

Building configuration...
!! IOS XR Configuration 4.3.2
!! Last configuration change at Mon Dec 12 08:46:36 2016 by admin
!
hw-module subslot 0/0/0 shutdown unpowered
hostname LAX-POP
banner motd ^
PLEASE DO NOT CHANGE CONFIG - CURRENTLY UNDERGOING IP-SLA TESTING.
PLEASE DO NOT CHANGE SPIRENT IP TO 10.1.1.0/24.  KEEP IT AS 10.11.11.0/24.
^
logging trap informational
logging console informational
logging monitor debugging
logging buffered 8000000
logging facility local7
logging 10.26.100.137 vrf default severity info
logging 10.76.230.204 vrf default severity info
logging hostnameprefix LAX-POP
service timestamps log datetime localtime
event manager environment EEM_CPU_threshold 20
event manager environment EEM_CPU_is_running 0
event manager environment EEM_CPU_timewindow 900
event manager environment EEM_CPU_storage_location harddisk:/EEM
event manager directory user policy harddisk:/EEM
event manager policy DTVScript.v1.0.tcl username admin persist-time 3600 type user
event manager policy DTVScript_health.v1.0.tcl username admin persist-time 3600 type user
event manager policy DTVScript_MeetPOP.v1.0.tcl username admin persist-time 3600 type user
event manager policy sh_process_cpu_Readable.tcl username admin persist-time 3600
event manager policy DTVScript_health_bw.v1.0.tcl username admin persist-time 3600 type user
telnet vrf MGMT ipv4 server max-servers 100
tftp vrf default ipv4 server homedir disk0:/iosxr-infra-4.3.2/lc/0x500064/bin max-servers 5
telnet vrf default ipv4 server max-servers 100
radius-server host 10.27.145.11 auth-port 1812 acct-port 1813
 timeout 5
!
radius-server key 7 02050D4808090C335F
radius-server timeout 10
radius-server retransmit 5
username admin2
 group root-system
 group cisco-support
 secret 5 $1$6Ro4$eGDR/j.33mp3iGCwDRAch0
!
aaa authorization eventmanager default local
cdp
vrf MGMT
 address-family ipv4 unicast
 !
!
vrf TEST
 address-family ipv4 unicast
 !
!
vrf Cust_A
 address-family ipv4 unicast
  import route-target
   10:10
   100:100
  !
  export route-target
   10:10
   100:100
  !
 !
!
line console
 exec-timeout 0 0
!
line default
 exec-timeout 0 0
 session-timeout 0
!
vty-pool eem 100 105
snmp-server ifmib ifalias long
snmp-server ifindex persist
snmp-server ifmib stats cache
snmp-server host 172.25.175.33 traps version 2c encrypted 15042A0F0F190D147C0B3809222F300154
snmp-server ipv4 dscp af22
snmp-server view instdir 1 included
snmp-server view instdir 1.3.6.1.4.1.9.9.249 excluded
snmp-server community msdc wandl
snmp-server community pmoss_community RO SNMPTesting
snmp-server traps rf
snmp-server traps bfd
snmp-server traps copy-complete
snmp-server traps snmp linkup
snmp-server traps snmp linkdown
snmp-server traps snmp coldstart
snmp-server traps snmp warmstart
snmp-server traps snmp authentication
snmp-server traps flash removal
snmp-server traps flash insertion
snmp-server traps sonet
snmp-server traps config
snmp-server traps entity
snmp-server timeouts subagent 20
snmp-server trap-source Loopback0
ftp client password encrypted 070E25414707
ftp client username admin
ftp client source-interface Null0
ntp
 server 10.68.255.1 version 4
 source Loopback0
 update-calendar
!
tftp client source-interface SRP0/3/0/0
ipv4 access-list ALL
 10 permit ipv4 any any
!
ipv4 access-list TAC
 10 permit ipv4 host 10.68.11.255 host 10.68.9.255
 20 permit ipv4 host 10.68.9.255 host 10.68.11.255
 30 permit ipv4 any any
!
ipv4 access-list pbr_acl
 10 permit ipv4 any host 10.68.255.1 log nexthop1 ipv4 10.16.13.2
 20 permit ipv4 any any log
!
ipv4 access-list SSM-Range
 5 permit ipv4 239.10.0.0 0.0.255.255 any
 7 permit ipv4 239.12.0.0 0.0.255.255 any
 20 permit ipv4 225.0.0.0 0.255.255.255 any
 40 permit ipv4 239.20.0.0 0.0.255.255 any log
 50 permit ipv4 any 239.20.0.0 0.0.255.255 log
!
ipv4 access-list Any-IP-Packet
 10 permit ipv4 any any
!
class-map match-any Telemetry
 match mpls experimental topmost 5
 end-class-map
!
class-map match-any LCF-Control
 match dscp af21 af22
 end-class-map
!
class-map match-any OnAir-Video
 match dscp af41
 end-class-map
!
class-map match-any test-packet
 match access-group ipv4 ALL
 end-class-map
!
class-map match-any Any-IP-Packet
 match access-group ipv4 Any-IP-Packet
 end-class-map
!
class-map match-any S-MUX-Control
 match dscp af43
 end-class-map
!
class-map match-any Network-Control
 match dscp cs6 cs7
 end-class-map
!
class-map match-any OnAir-Video-test
 match access-group ipv4 ANY
 end-class-map
!
class-map match-any Network-Management
 match dscp af22
 end-class-map
!
policy-map GE
 class OnAir-Video
  police rate percent 90
  !
  priority level 1
 !
 class Network-Control
  bandwidth percent 2
 !
 class S-MUX-Control
  bandwidth percent 2
 !
 class LCF-Control
  bandwidth percent 3
  random-detect dscp af21 2000 packets 3000 packets
  random-detect dscp af22 1000 packets 2000 packets
 !
 class Any-IP-Packet
  bandwidth percent 1
 !
 class class-default
  bandwidth percent 2
 !
 end-policy-map
!
policy-map 10GE
 class OnAir-Video
  police rate percent 90
  !
  priority level 1
 !
 class Network-Control
  queue-limit 150000 packets
  bandwidth percent 2
 !
 class S-MUX-Control
  queue-limit 150000 packets
  bandwidth percent 2
 !
 class LCF-Control
  queue-limit 150000 packets
  bandwidth percent 3
  random-detect dscp af21 3000 packets 4000 packets
  random-detect dscp af22 2000 packets 3000 packets
 !
 class Any-IP-Packet
  queue-limit 150000 packets
  bandwidth percent 1
 !
 class Telemetry
  bandwidth percent 1
  queue-limit 150000 packets
 !
 class class-default
  queue-limit 150000 packets
  bandwidth percent 1
 !
 end-policy-map
!
policy-map GE-test
 class OnAir-Video-test
  set dscp af41
  police rate percent 90
  !
 !
 class class-default
 !
 end-policy-map
!
policy-map POS-OC3
 class OnAir-Video
  police rate percent 90
  !
  priority level 1
 !
 class Network-Control
  bandwidth percent 2
 !
 class S-MUX-Control
  bandwidth percent 2
 !
 class class-default
 !
 end-policy-map
!
policy-map SRP-OC192
 class OnAir-Video
  set srp-priority 4
  police rate percent 90
  !
  priority level 1
 !
 class Telemetry
  set srp-priority 6
  police rate percent 1
  !
  priority level 1
 !
 class class-default
  bandwidth percent 1
 !
 end-policy-map
!
policy-map test-packet
 class test-packet
  set dscp af41
 !
 class class-default
 !
 end-policy-map
!
policy-map SRP-OC192-test
 class OnAir-Video
  set srp-priority 4
  police rate percent 90
  !
  priority level 1
 !
 class Network-Control
  set srp-priority 6
  police rate percent 3
  !
  priority level 1
 !
 class S-MUX-Control
  queue-limit 150000 packets
  bandwidth percent 2
 !
 class Network-Management
  queue-limit 150000 packets
  bandwidth percent 2
 !
 class Any-IP-Packet
  queue-limit 150000 packets
  bandwidth percent 1
 !
 class class-default
  queue-limit 150000 packets
  bandwidth percent 2
 !
 end-policy-map
!
hw-module port 0 srp location 0/3/CPU0 spa-bay 0
hw-module port 0 srp location 0/3/CPU0 spa-bay 1
hw-module port 0 srp location 0/3/CPU0 spa-bay 2
hw-module port 0 srp location 0/3/CPU0 spa-bay 5
hw-module port 0 srp location 0/7/CPU0 spa-bay 0
hw-module port 0 srp location 0/7/CPU0 spa-bay 1
hw-module port 0 srp location 0/7/CPU0 spa-bay 2
hw-module port 0 srp location 0/7/CPU0 spa-bay 5
interface Bundle-Ether100
!
interface Loopback0
 ipv4 address 10.68.255.4 255.255.255.255
!
interface Loopback1
 ipv4 address 10.68.254.4 255.255.255.255
!
interface Loopback2
 ipv4 address 10.68.176.241 255.255.255.255
!
interface Loopback3
 ipv4 address 10.68.253.2 255.255.255.255
!
interface Loopback4
 ipv4 address 10.68.252.4 255.255.255.255
!
interface Loopback100
 description *| Loopback 100 | LBK | ISW | VIRTUAL | AT&T |*
 ipv4 address 10.101.255.3 255.255.255.255
!
interface Loopback200
 description *| Loopback 200 | LBK | ISW | VIRTUAL | AT&T |*
 ipv4 address 10.102.255.3 255.255.255.255
!
interface Loopback225
 vrf Cust_A
 ipv4 address 55.55.55.55 255.255.255.255
!
interface Loopback300
 description *| Loopback 300 | LBK | ISW | VIRTUAL | AT&T |*
 ipv4 address 10.103.255.3 255.255.255.255
!
interface Loopback333
 ipv4 address 47.47.47.47 255.255.255.255
!
interface Loopback399
 vrf Cust_A
 ipv4 address 9.9.9.9 255.255.255.255
!
interface Loopback400
 description *| Loopback 400 | LBK | ISW | VIRTUAL | AT&T |*
 ipv4 address 10.104.255.3 255.255.255.255
!
interface Loopback666
 description *|BLWV04TVSAC7601LBV POS3/0/0|OC3|ISW|OC3-17160702|CENLINK|*
!
interface tunnel-ip1
 ipv4 mtu 1600
 ipv4 address 1.1.1.1 255.255.255.252
 tunnel mode gre ipv4
 tunnel source 10.7.1.1
 tunnel destination 10.0.0.99
!
interface tunnel-ip33
 ipv4 address 1.1.1.5 255.255.255.252
 tunnel mode gre ipv4
 tunnel source TenGigE0/0/2/0
 keepalive 1 3
 tunnel destination 10.16.13.2
!
interface MgmtEth0/RP0/CPU0/0
 ipv4 address 10.26.64.1 255.255.255.0
!
interface MgmtEth0/RP1/CPU0/0
 vrf MGMT
 ipv4 address 10.26.64.5 255.255.255.0
!
interface GigabitEthernet0/0/5/0
 description ***** LAXP DEN-LCF-B, Gig4/46 *****\
 ipv4 address 172.16.16.1 255.255.255.252
!
interface GigabitEthernet0/0/5/1
 description ***** LAXP DEN-LCF-A *****
!
interface GigabitEthernet0/0/5/2
 ipv4 address 10.68.77.5 255.255.255.252
!
interface GigabitEthernet0/0/5/3
 ipv4 address 75.75.75.1 255.255.255.0
!
interface GigabitEthernet0/0/5/4
 mtu 1532
 ipv4 address 10.10.10.2 255.255.255.252
 ipv4 access-group pbr_acl ingress hardware-count
!
interface GigabitEthernet0/0/5/5
 load-interval 30
 capture software packets
!
interface GigabitEthernet0/0/5/6
 description SDN test to Spirent7/6
 ipv4 address 17.1.1.1 255.255.255.0
 negotiation auto
!
interface GigabitEthernet0/0/5/7
 ipv4 address 30.30.30.30 255.255.255.0
!
interface TenGigE0/0/1/0
 description connected to 3/4, Spirent
 ipv4 address 10.139.139.1 255.255.255.252
!
interface TenGigE0/0/2/0
 description Connected to Te2/2 on MSDC-7604-VE-02B
 cdp
 mtu 9000
 ipv4 address 10.16.13.1 255.255.255.0
 load-interval 30
!
interface TenGigE0/0/3/0
 description LA3 TO NGBN
 ipv4 address 10.26.126.142 255.255.255.252
 load-interval 30
!
interface TenGigE0/0/4/0
 description Connected to Te2/2 on MSDC-7604-VE-01P
 ipv4 address 172.16.15.1 255.255.255.252
!
controller SONET0/3/0/0
 path
  threshold b3-tca 9
 !
 threshold b1-tca 9
 threshold b2-tca 9
 clock source internal
!
controller SONET0/3/1/0
 path
  threshold b3-tca 9
 !
 threshold b1-tca 9
 threshold b2-tca 9
 clock source internal
!
controller SONET0/3/2/0
 path
  threshold b3-tca 9
 !
 threshold b1-tca 9
 threshold b2-tca 9
 clock source internal
!
controller SONET0/3/5/0
 path
  threshold b3-tca 9
 !
 threshold b1-tca 9
 threshold b2-tca 9
 clock source internal
!
controller SONET0/7/0/0
 path
  threshold b3-tca 9
 !
 threshold b1-tca 9
 threshold b2-tca 9
 clock source internal
!
controller SONET0/7/1/0
 path
  threshold b3-tca 9
 !
 threshold b1-tca 9
 threshold b2-tca 9
 clock source internal
!
controller SONET0/7/2/0
 path
  threshold b3-tca 9
 !
 threshold b1-tca 9
 threshold b2-tca 9
 clock source internal
!
controller SONET0/7/5/0
 path
  threshold b3-tca 9
 !
 threshold b1-tca 9
 threshold b2-tca 9
 clock source internal
!
controller wanphy0/0/1/0
 lanmode on
!
interface SRP0/3/0/0
 description LAXP 1st 10Gig West
 cdp
 ipv4 address 10.68.2.1 255.255.255.0
 ipv4 directed-broadcast
 load-interval 30
 ipv4 access-group TAC egress hardware-count interface-statistics
!
interface SRP0/3/2/0
 description LAXP 2nd 10Gig West
 service-policy output SRP-OC192-test
 ipv4 address 10.99.1.1 255.255.255.0
 load-interval 30
!
interface SRP0/7/0/0
 description LAXP 4th 10Gig East
 ipv4 address 10.100.2.1 255.255.255.0
 load-interval 30
!
interface SRP0/7/2/0
 description LAXP 4th 10Gig West
 service-policy output SRP-OC192
 ipv4 address 10.231.1.1 255.255.255.0
 load-interval 30
!
interface preconfigure POS0/0/0/0
 shutdown
!
interface preconfigure POS0/0/0/1
 shutdown
!
interface preconfigure POS0/0/0/2
 shutdown
!
interface preconfigure POS0/0/0/3
 shutdown
!
interface preconfigure POS0/3/0/0
 shutdown
!
interface preconfigure POS0/3/1/0
 shutdown
!
interface preconfigure POS0/3/2/0
 shutdown
!
interface preconfigure POS0/3/5/0
 shutdown
!
interface preconfigure POS0/7/0/0
 shutdown
!
interface preconfigure POS0/7/1/0
 shutdown
!
controller preconfigure SONET0/0/0/0
 path
  scrambling disable
 !
 report lais
 overhead s1s0 2
 threshold b2-tca 4
 clock source internal
!
controller preconfigure SONET0/0/0/1
 path
  scrambling disable
 !
 report lais
 overhead s1s0 2
 threshold b2-tca 4
 clock source internal
!
controller preconfigure SONET0/0/0/2
 path
  scrambling disable
 !
 report lais
 overhead s1s0 2
 threshold b2-tca 4
 clock source internal
!
rd-set TEST
  1:100
end-set
!
prefix-set PASS
  0.0.0.0/0 le 32
end-set
!
prefix-set TEST1
  10.68.11.56/30,
  10.68.11.255/32,
  11.21.1.1/32,
  10.16.13.0/24
end-set
!
prefix-set TEST2
  10.109.112.36/30
end-set
!
prefix-set TESTBFD
  72.16.18.0/24,
  172.16.17.0/30,
  172.16.16.0/30,
  53.53.53.53/32
end-set
!
prefix-set BC_PREFIX
  10.68.128.0/24 le 32,
  10.68.129.0/24,
  10.68.144.0/24 le 32,
  10.68.3.0/24,
  10.68.145.0/24,
  10.68.2.255/32,
  10.68.1.255/32,
  10.2.1.0/24,
  10.68.128.0/24 le 32,
  10.68.129.0/24,
  10.68.144.0/24 le 32,
  10.68.3.0/24,
  10.68.145.0/24,
  10.68.2.255/32,
  10.68.1.255/32,
  10.2.1.0/24,
  10.2.1.0/24
end-set
!
prefix-set ROS_PREFIX
end-set
!
prefix-set IP_SLA_Ring1
  192.168.16.1/32,
  10.28.103.0/24,
  10.28.104.0/24,
  10.28.144.0/24,
  10.28.145.0/24,
  10.28.165.0/24,
  10.23.225.0/26,
  10.8.70.0/24,
  10.8.71.0/24,
  10.88.100.0/24
end-set
!
prefix-set IP_SLA_RING_1
  10.1.1.1/32
end-set
!
prefix-set IP_SLA_RING_2
  10.2.2.1/32
end-set
!
prefix-set IP_SLA_RING_3
  10.3.3.1/32
end-set
!
prefix-set IP_SLA_RING_4
  10.4.4.1/32
end-set
!
prefix-set LCF_MGMT_PREFIX
  10.194.17.0/24 le 32,
  10.194.5.0/24 le 32
end-set
!
prefix-set LCF_VIDEO_PREFIX
  10.94.17.0/24 le 32,
  10.94.5.0/24 le 32,
  10.94.16.0/24 le 32
end-set
!
prefix-set STATIC_DWDM_MSDC
  10.8.70.0/24,
  10.8.71.0/24,
  10.8.0.0/16,
  10.28.103.0/24,
  10.28.104.0/24,
  10.28.144.0/24,
  10.28.145.0/24,
  10.28.165.0/24,
  10.23.225.0/26,
  172.31.63.0/24,
  10.13.51.0/24
end-set
!
route-policy PASS
  if destination in PASS then
    pass
  endif
end-policy
!
route-policy TEST1
  if destination in TEST1 then
    pass
  endif
end-policy
!
route-policy TEST2
  if destination in TEST2 then
    pass
  endif
end-policy
!
route-policy TESTBFD
  if destination in TESTBFD then
    pass
  endif
end-policy
!
route-policy device4
  if source in (10.2.1.2/32) then
    pass
  else
    drop
  endif
end-policy
!
route-policy device5
  if source in (10.2.1.3/32) then
    pass
  else
    drop
  endif
end-policy
!
route-policy device6
  if source in (10.2.1.4/32) then
    pass
  else
    drop
  endif
end-policy
!
route-policy device7
  if source in (10.2.1.5/32) then
    pass
  else
    drop
  endif
end-policy
!
route-policy BC_LCF_DENY
  if destination in BC_PREFIX or destination in LCF_VIDEO_PREFIX or destination in LCF_MGMT_PREFIX then
    drop
  else
    pass
  endif
end-policy
!
route-policy IPSLA_Ring1
  if destination in IP_SLA_Ring1 then
    pass
  endif
end-policy
!
route-policy BC_LCF_REDIST
  if destination in BC_PREFIX or destination in ROS_PREFIX or destination in LCF_VIDEO_PREFIX or destination in LCF_MGMT_PREFIX then
    pass
  endif
end-policy
!
route-policy IP_SLA_RING_1
  if destination in IP_SLA_RING_1 then
    pass
  endif
end-policy
!
route-policy IP_SLA_RING_2
  if destination in IP_SLA_RING_2 then
    pass
  endif
end-policy
!
route-policy IP_SLA_RING_3
  if destination in IP_SLA_RING_3 then
    pass
  endif
end-policy
!
route-policy IP_SLA_RING_4
  if destination in IP_SLA_RING_4 then
    pass
  endif
end-policy
!
route-policy STATIC_DWDM_MSDC
  if destination in STATIC_DWDM_MSDC then
    pass
  endif
end-policy
!
route-policy BC_LCF_MGMT_REDIST
  if destination in BC_PREFIX or destination in ROS_PREFIX or destination in LCF_MGMT_PREFIX then
    pass
  endif
end-policy
!
router static
 address-family ipv4 unicast
  0.0.0.0/0 10.26.64.254
  1.1.1.1/32 GigabitEthernet0/0/5/2 10.68.77.5 description test
  10.1.1.1/32 10.68.128.2
  10.2.2.1/32 10.68.128.2
  10.3.3.1/32 10.68.128.2
  10.4.4.1/32 10.68.128.2
  10.8.0.0/16 10.26.126.141
  10.8.70.0/24 10.26.126.141
  10.8.71.0/24 10.26.126.141
  10.10.10.0/24 10.140.142.18
  10.13.51.0/24 10.26.126.141
  10.23.225.0/26 10.26.126.141
  10.26.124.14/32 10.26.126.141
  10.28.1.0/30 10.26.126.141
  10.28.103.0/24 10.26.126.141
  10.28.104.0/24 10.26.126.141
  10.28.144.0/24 10.26.126.141
  10.28.145.0/24 10.26.126.141
  10.28.165.0/24 10.26.126.141
  10.68.12.0/24 10.68.2.2
  10.68.253.1/32 10.99.1.2
  10.68.254.1/32 10.100.2.2
  10.70.12.0/24 10.68.128.2
  10.88.100.0/24 10.26.126.141
  10.99.12.3/32 10.231.1.2
  10.100.12.2/32 10.68.2.2
  10.213.12.3/32 10.68.2.2
  47.47.47.47/32 GigabitEthernet0/0/5/4 10.10.10.1 bfd fast-detect minimum-interval 100 multiplier 3
  55.55.55.55/32 10.16.13.2 bfd fast-detect minimum-interval 100 multiplier 3
  99.136.180.0/22 10.140.142.18
  172.31.63.0/24 10.26.126.141
  192.168.0.0/16 10.68.2.2
  192.168.16.1/32 10.68.128.2
 !
 vrf MGMT
  address-family ipv4 unicast
   0.0.0.0/0 10.26.64.254
  !
 !
!
router isis AVB1
 interface GigabitEthernet0/0/5/0
  bfd minimum-interval 100
  bfd multiplier 3
  bfd fast-detect ipv4
  point-to-point
  hello-padding sometimes
  address-family ipv4 unicast
   metric 100
  !
 !
 interface GigabitEthernet0/0/5/1
  bfd minimum-interval 100
  bfd multiplier 3
  bfd fast-detect ipv4
  point-to-point
  hello-padding sometimes
  address-family ipv4 unicast
   metric 100
  !
 !
!
router isis DIRECTV
 is-type level-2-only
 net 49.0300.0300.0100.6825.5004.00
 nsf cisco
 log adjacency changes
 lsp-gen-interval maximum-wait 5000 initial-wait 50 secondary-wait 200
 log pdu drops
 lsp-refresh-interval 65000
 max-lsp-lifetime 65535
 address-family ipv4 unicast
  metric-style wide
  spf-interval maximum-wait 5000 initial-wait 50 secondary-wait 200
  redistribute static route-policy IPSLA_Ring1
  redistribute isis DIRECTV2 route-policy TEST2 metric-type rib-metric-as-internal
  redistribute isis DIRECTV11 route-policy BC_LCF_DENY metric-type rib-metric-as-internal
 !
 interface Loopback100
  passive
  address-family ipv4 unicast
  !
 !
 interface GigabitEthernet0/0/5/3
  address-family ipv4 unicast
   metric 10
  !
 !
 interface GigabitEthernet0/0/5/5
  address-family ipv4 unicast
  !
 !
 interface TenGigE0/0/1/0.10
  bfd minimum-interval 100
  bfd multiplier 3
  bfd fast-detect ipv4
  priority 100
  point-to-point
  hello-padding sometimes
  address-family ipv4 unicast
   metric 200
  !
 !
 interface SRP0/3/0/0
  bfd minimum-interval 100
  bfd multiplier 3
  bfd fast-detect ipv4
  priority 100
  hello-padding sometimes
  address-family ipv4 unicast
   metric 10
  !
 !
!
router isis DIRECTV2
 is-type level-2-only
 net 49.0300.0300.0100.6825.4004.00
 log adjacency changes
 lsp-gen-interval maximum-wait 5000 initial-wait 50 secondary-wait 200
 log pdu drops
 lsp-refresh-interval 65000
 max-lsp-lifetime 65535
 address-family ipv4 unicast
  metric-style wide
  metric 1
  spf-interval maximum-wait 5000 initial-wait 50 secondary-wait 200
  redistribute connected route-policy TEST1 metric-type rib-metric-as-internal
  redistribute static route-policy IP_SLA_RING_2 metric-type rib-metric-as-internal
  redistribute isis DIRECTV11 route-policy TEST1 metric-type rib-metric-as-internal
 !
 interface Loopback0
  passive
  address-family ipv4 unicast
  !
 !
 interface Loopback1
  passive
  address-family ipv4 unicast
  !
 !
 interface Loopback200
  passive
  address-family ipv4 unicast
  !
 !
 interface GigabitEthernet0/0/5/7
  passive
  bfd minimum-interval 100
  bfd multiplier 3
  point-to-point
  hello-padding sometimes
  address-family ipv4 unicast
   metric 10
  !
 !
 interface TenGigE0/0/1/0
  bfd minimum-interval 100
  bfd multiplier 3
  bfd fast-detect ipv4
  point-to-point
  hello-padding sometimes
  address-family ipv4 unicast
   metric 200
  !
 !
 interface TenGigE0/0/1/0.20
  bfd minimum-interval 100
  bfd multiplier 3
  bfd fast-detect ipv4
  priority 100
  point-to-point
  hello-padding sometimes
  address-family ipv4 unicast
   metric 10
  !
 !
 interface TenGigE0/0/3/0.99
  passive
  bfd minimum-interval 100
  bfd multiplier 3
  point-to-point
  hello-padding sometimes
  address-family ipv4 unicast
   metric 10
  !
 !
 interface TenGigE0/0/3/0.200
  bfd minimum-interval 100
  bfd multiplier 3
  point-to-point
  hello-padding sometimes
  address-family ipv4 unicast
   metric 10
  !
 !
 interface TenGigE0/0/3/0.213
  passive
  address-family ipv4 unicast
   metric 10
  !
 !
 interface SRP0/3/2/0
  bfd minimum-interval 100
  bfd multiplier 3
  bfd fast-detect ipv4
  priority 100
  hello-padding sometimes
  address-family ipv4 unicast
   metric 10
  !
 !
!
router isis DIRECTV3
 address-family ipv4 unicast
 !
 interface Loopback300
  passive
  address-family ipv4 unicast
  !
 !
!
router isis DIRECTV4
 is-type level-2-only
 net 49.0300.0300.0100.6825.2004.00
 nsf cisco
 log adjacency changes
 lsp-gen-interval maximum-wait 5000 initial-wait 50 secondary-wait 200
 log pdu drops
 lsp-refresh-interval 65000
 max-lsp-lifetime 65535
 address-family ipv4 unicast
  metric-style wide
  metric 100
  spf-interval maximum-wait 5000 initial-wait 50 secondary-wait 200
  redistribute static route-policy IP_SLA_RING_4 metric-type rib-metric-as-internal
  redistribute isis DIRECTV11 route-policy BC_LCF_MGMT_REDIST metric-type rib-metric-as-external
 !
 interface Loopback4
  passive
  address-family ipv4 unicast
  !
 !
 interface Loopback400
  passive
  address-family ipv4 unicast
  !
 !
 interface TenGigE0/0/1/0.40
  bfd minimum-interval 100
  bfd multiplier 3
  bfd fast-detect ipv4
  priority 100
  point-to-point
  hello-padding sometimes
  address-family ipv4 unicast
   metric 10
  !
 !
!
router isis DIRECTV11
 is-type level-2-only
 net 49.0300.0300.0100.6817.6241.00
 nsf cisco
 log adjacency changes
 lsp-gen-interval maximum-wait 5000 initial-wait 50 secondary-wait 200
 log pdu drops
 lsp-refresh-interval 65000
 max-lsp-lifetime 65535
 address-family ipv4 unicast
  metric-style wide
  spf-interval maximum-wait 5000 initial-wait 50 secondary-wait 200
  redistribute static route-policy STATIC_DWDM_MSDC metric-type rib-metric-as-internal
  redistribute isis DIRECTV route-policy BC_LCF_DENY metric-type rib-metric-as-internal
  redistribute isis DIRECTV2 route-policy BC_LCF_DENY metric-type rib-metric-as-internal
  redistribute isis DIRECTV4 route-policy BC_LCF_DENY metric-type rib-metric-as-internal
  redistribute isis DIRECTV12 route-policy BC_LCF_DENY metric-type rib-metric-as-internal
 !
 interface Loopback2
  passive
  address-family ipv4 unicast
  !
 !
 interface TenGigE0/0/2/0
  bfd minimum-interval 100
  bfd multiplier 3
  point-to-point
  hello-padding sometimes
  address-family ipv4 unicast
   metric 10
  !
 !
 interface TenGigE0/0/4/0
  bfd minimum-interval 100
  bfd multiplier 3
  point-to-point
  hello-padding sometimes
  address-family ipv4 unicast
   metric 10
  !
 !
!
router isis DIRECTV12
 is-type level-2-only
 net 49.0300.0300.0100.6825.3022.00
 nsf cisco
 log adjacency changes
 lsp-gen-interval maximum-wait 5000 initial-wait 50 secondary-wait 200
 log pdu drops
 lsp-refresh-interval 65000
 max-lsp-lifetime 65535
 address-family ipv4 unicast
  metric-style wide
  spf-interval maximum-wait 5000 initial-wait 50 secondary-wait 200
  redistribute static route-policy IP_SLA_RING_3 metric-type rib-metric-as-internal
  redistribute isis DIRECTV11 route-policy BC_LCF_MGMT_REDIST metric-type rib-metric-as-external
 !
 interface Loopback3
  passive
  address-family ipv4 unicast
  !
 !
 interface GigabitEthernet0/0/5/6
  address-family ipv4 unicast
  !
 !
 interface TenGigE0/0/1/0.30
 !
 interface TenGigE0/0/2/0.67
  bfd minimum-interval 100
  bfd multiplier 3
  point-to-point
  hello-padding sometimes
  address-family ipv4 unicast
   metric 10
  !
 !
 interface TenGigE0/0/2/0.100
  passive
  bfd minimum-interval 100
  bfd multiplier 3
  point-to-point
  hello-padding sometimes
  address-family ipv4 unicast
   metric 10
  !
 !
 interface TenGigE0/0/3/0.100
  passive
  bfd minimum-interval 100
  bfd multiplier 3
  point-to-point
  hello-padding sometimes
  address-family ipv4 unicast
   metric 10
  !
 !
 interface SRP0/7/0/0
  bfd minimum-interval 100
  bfd multiplier 3
  bfd fast-detect ipv4
  priority 100
  hello-padding sometimes
  address-family ipv4 unicast
   metric 10
  !
 !
!
snmp-server traps isis adjacency-change
snmp-server traps bgp cbgp2
router bgp 12050
 bgp graceful-restart restart-time 120
 bgp graceful-restart graceful-reset
 bgp graceful-restart stalepath-time 360
 bgp graceful-restart
 address-family ipv4 unicast
 !
 address-family vpnv4 unicast
  nexthop trigger-delay critical 0
  nexthop trigger-delay non-critical 5000
 !
 neighbor 10.112.47.254
  remote-as 12050
  update-source Loopback0
  address-family vpnv4 unicast
  !
 !
 neighbor 10.112.47.255
  remote-as 12050
  update-source Loopback0
  address-family vpnv4 unicast
  !
 !
 vrf Cust_A
  rd 100:100
  address-family ipv4 unicast
   redistribute connected
  !
 !
!
snmp-server traps mpls ldp up
snmp-server traps mpls ldp down
mpls ldp
 router-id 10.68.255.4
 interface GigabitEthernet0/0/5/0
 !
 interface TenGigE0/0/2/0
 !
 interface TenGigE0/0/3/0
 !
 interface SRP0/3/0/0
 !
 interface SRP0/3/2/0
 !
 interface SRP0/7/0/0
 !
 interface SRP0/7/2/0
 !
!
snmp-server traps pim neighbor-change
snmp-server traps pim invalid-message-received
snmp-server traps pim rp-mapping-change
snmp-server traps pim interface-state-change
snmp-server traps msdp peer-state-change
multicast-routing
 address-family ipv4
  nsf
  log-traps
  multipath hash source
  ssm range SSM-Range
  oom-handling
  rate-per-route
  interface all enable
  accounting per-prefix
 !
!
router igmp
 vrf default
 !
 interface SRP0/3/0/0
  explicit-tracking
  version 3
  router enable
 !
 interface Loopback100
  explicit-tracking
  version 3
  join-group 239.235.255.1 10.101.255.1
  join-group 239.235.255.2 10.101.255.2
  join-group 239.235.255.3 10.101.255.3
  router enable
 !
 interface Loopback200
  explicit-tracking
  version 3
  join-group 239.20.20.1 20.20.20.20
  join-group 239.236.255.1 10.102.255.1
  join-group 239.236.255.2 10.102.255.2
  join-group 239.236.255.3 10.102.255.3
  router enable
 !
 interface Loopback300
  explicit-tracking
  version 3
  join-group 239.237.255.1 10.103.255.1
  join-group 239.237.255.2 10.103.255.2
  join-group 239.237.255.3 10.103.255.3
  router enable
 !
 interface Loopback400
  explicit-tracking
  version 3
  join-group 239.238.255.1 10.104.255.1
  join-group 239.238.255.2 10.104.255.2
  join-group 239.238.255.3 10.104.255.3
  router enable
 !
 interface TenGigE0/0/3/0
  explicit-tracking
  version 3
  router enable
 !
 interface TenGigE0/0/3/0.68
  explicit-tracking
  version 3
  router enable
 !
 interface TenGigE0/0/3/0.99
  explicit-tracking
  version 3
  router enable
 !
 interface TenGigE0/0/3/0.100
  explicit-tracking
  version 3
  router enable
 !
 interface TenGigE0/0/3/0.213
  explicit-tracking
  version 3
  router enable
 !
 interface GigabitEthernet0/0/5/5
  explicit-tracking
  version 3
  static-group 239.239.10.10 40.40.40.56
  router enable
 !
 interface GigabitEthernet0/0/5/7
 !
!
ssh server v2
ssh timeout 120
snmp-server traps fabric plane
snmp-server traps fabric bundle link
snmp-server traps fabric bundle state
xml agent ssl
 session timeout 5
!
snmp-server traps sensor
snmp-server traps fru-ctrl
ipsla
 operation 1
  type icmp echo
   source address 10.101.255.3
   destination address 10.101.255.1
   datasize request 1500
   frequency 100
  !
 !
 operation 2
  type icmp echo
   source address 10.101.255.3
   destination address 10.101.255.2
   datasize request 1500
   frequency 100
  !
 !
 operation 3
  type icmp echo
   source address 10.102.255.3
   destination address 10.102.255.1
   datasize request 1500
   frequency 100
  !
 !
 operation 4
  type icmp echo
   source address 10.102.255.3
   destination address 10.102.255.2
   datasize request 1500
   frequency 100
  !
 !
 operation 5
  type icmp echo
   source address 10.103.255.3
   destination address 10.103.255.1
   datasize request 1500
   frequency 100
  !
 !
 operation 6
  type icmp echo
   source address 10.103.255.3
   destination address 10.103.255.2
   datasize request 1500
   frequency 100
  !
 !
 schedule operation 1
  start-time now
  life forever
 !
 schedule operation 2
  start-time now
  life forever
 !
 schedule operation 3
  start-time now
  life forever
 !
 schedule operation 4
  start-time now
  life forever
 !
 schedule operation 5
  start-time now
  life forever
 !
 schedule operation 6
  start-time now
  life forever
 !
!
performance-mgmt thresholds node cpu template CPU_MONITOR
 AverageCpuUsed ge 60
 sample-interval 5
!
performance-mgmt thresholds node process template NODE_PROCESS
 AverageCPUUsed gt 40
 sample-interval 1
!
performance-mgmt apply thresholds node process location 0/RP0/CPU0 NODE_PROCESS
router pim
 address-family ipv4
  nsf lifetime 60
  interface Loopback0
   enable
  !
  interface Loopback1
   enable
  !
  interface Loopback2
   enable
  !
  interface Loopback3
   enable
  !
  interface SRP0/3/0/0
   enable
  !
  interface SRP0/3/2/0
   enable
  !
  interface SRP0/7/0/0
   enable
  !
  interface SRP0/7/2/0
   enable
  !
  interface Loopback100
   enable
  !
  interface Loopback200
   enable
  !
  interface Loopback300
   enable
  !
  interface Loopback400
   enable
  !
  interface TenGigE0/0/1/0
   enable
  !
  interface TenGigE0/0/2/0
   enable
  !
  interface TenGigE0/0/4/0
   enable
  !
  interface TenGigE0/0/1/0.10
   enable
  !
  interface TenGigE0/0/1/0.20
   enable
  !
  interface TenGigE0/0/1/0.30
   enable
  !
  interface TenGigE0/0/1/0.40
   enable
  !
  interface GigabitEthernet0/0/5/0
  !
  interface GigabitEthernet0/0/5/3
   enable
  !
  interface GigabitEthernet0/0/5/5
   enable
  !
 !
!
end

EOF
