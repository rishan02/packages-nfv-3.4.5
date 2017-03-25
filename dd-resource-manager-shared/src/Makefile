JAVA_PACKAGE = com.cisco.singtel.resourcemanager
JDIR = $(shell echo $(JAVA_PACKAGE) | sed 's/\./\//g')

NS  = namespaces

JFLAGS = --java-disable-prefix \
		 --exclude-enums \
		 --fail-on-warnings \
		 --java-package $(JAVA_PACKAGE).$(NS) \
		 --emit-java java/src/$(JDIR)/namespaces

all: setup preyang fxs extenal-ns postyang javac

# Include standard NCS examples build definitions and rules
include $(NCS_DIR)/src/ncs/build/include.ncs.mk

SRC = $(wildcard yang/*.yang)
FXS = $(SRC:yang/resource-manager.yang=../load-dir/resource-manager.fxs)
EXTERNAL_NS =

setup:
		mkdir -p java/src/$(JDIR)/namespaces
		mkdir -p ncsc-out
		cp ../../../external/foss/nso-tools* ../shared-jar/
		cp ../../../external/foss/guava* ../shared-jar/
		cp ../../../external/foss/commons-* ../shared-jar/
		#cp ../../../external/foss/gson* ../shared-jar/

preyang:
	perl -i -pe 's/\/\/\%service\%/uses ncs:service-data;\nncs:servicepoint /g' yang/*.yang

postyang:
	@if [ "$(YANG_GEN)" ]; then \
		rm -rf ./java/src/com/cisco/singtel/resourcemanager/model; \
		rm -rf ./java/src/com/cisco/singtel/resourcemanager/base; \
		$(YANG_GEN) \
			-C java-rw \
			-P singtel-rm,?ncs \
			-x com.cisco.singtel.resourcemanager.model \
			-X singtel-rm:com.cisco.singtel.resourcemanager.model \
			-B com.cisco.singtel.resourcemanager.base \
			-u ddrm \
			-b ./java/src \
			-d ./java/src yang/ $(NCS_DIR)/src/ncs/yang; \
	fi


fxs: $(FXS)
extenal-ns: $(EXTERNAL_NS)

../load-dir/resource-manager.fxs: yang/resource-manager.yang
	$(NCSC) -a yang/resource-manager.yang --yangpath ./yang/ -c -o $@ $<
	$(NCSC) $(JFLAGS)/resource-manager.java $@


$(EXTERNAL_NS):
	$(NCSC) --java-disable-prefix \
	        --exclude-enums \
	        --java-package com.tailf.nfvo.esc.namespaces \
	        --emit-java $@ $<

javac:
	cd java && ant -q all

clean:
	rm -rf  ../load-dir/* java/src/$(JDIR)/namespaces
	rm -f ../*jar/*.jar
	cd java && ant -q clean || true
	rm -f java/src/$(JDIR)/$(NS)/*.java
	rm -f $(EXTERNAL_NS)



