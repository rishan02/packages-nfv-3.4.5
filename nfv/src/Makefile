
JAVA_PACKAGE = com.singtel.nfv
JDIR = $(shell echo $(JAVA_PACKAGE) | sed 's/\./\//g')

NS  = namespaces

JFLAGS = --java-disable-prefix \
         --exclude-enums \
         --fail-on-warnings \
         --java-package $(JAVA_PACKAGE).$(NS) \
         --emit-java java/src/$(JDIR)/namespaces

JFLAGSENUM = --java-disable-prefix \
         --fail-on-warnings \
         --java-package $(JAVA_PACKAGE).$(NS) \
         --emit-java java/src/$(JDIR)/namespaces

all:	fxs javac

# Include standard NCS examples build definitions and rules
include $(NCS_DIR)/src/ncs/build/include.ncs.mk

SRC = $(wildcard yang/*.yang)
FXS = $(SRC:yang/%.yang=../load-dir/%.fxs)

YANGPATH = --yangpath ./yang \
        --yangpath ../../esc/src/ncsc-out/modules/yang \
        --yangpath ../../dd-resource-manager-shared/src/yang \
        --yangpath ../../Sites/src/yang \
	--yangpath ../../cisco-ios/src/ncsc-out/modules/yang

fxs:	$(FXS)

../load-dir/nfv-types.fxs:	yang/nfv-types.yang
	$(NCSC)  `ls $*-ann.yang  > /dev/null 2>&1 && echo "-a $*-ann.yang"` \
             $(YANGPATH) -c -o $@ $<
	$(NCSC) $(JFLAGSENUM)/nfv-types.java $@ 

../load-dir/nfv.fxs:	yang/nfv.yang
	$(NCSC)  `ls $*-ann.yang  > /dev/null 2>&1 && echo "-a $*-ann.yang"` \
             $(YANGPATH) -c -o $@ $<
	$(NCSC) $(JFLAGS)/nfv.java $@ 
	
../load-dir/vcpe-rpc.fxs:	yang/vcpe-rpc.yang
	$(NCSC)  `ls $*-ann.yang  > /dev/null 2>&1 && echo "-a $*-ann.yang"` \
             $(YANGPATH) -c -o $@ $<
	$(NCSC) $(JFLAGS)/vcpe-rpc.java $@ 

javac:
	cd java && ant -q all

clean:
	rm -rf  ../load-dir/*
	rm -f ../*jar/*.jar
	cd java && ant -q clean || true
	rm -f java/src/$(JDIR)/$(NS)/*.java
