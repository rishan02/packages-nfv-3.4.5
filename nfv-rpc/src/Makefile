JAVA_PACKAGE = com.singtel.nfv
JDIR = $(shell echo $(JAVA_PACKAGE) | sed 's/\./\//g')

NS  = namespaces

JFLAGS = --java-disable-prefix \
		 --exclude-enums \
		 --fail-on-warnings \
		 --java-package $(JAVA_PACKAGE).$(NS) \
		 --emit-java java/src/$(JDIR)/namespaces

all: setup postyang javac

# Include standard NCS examples build definitions and rules
include $(NCS_DIR)/src/ncs/build/include.ncs.mk

setup:
		mkdir -p java/src/$(JDIR)/namespaces
		mkdir -p ncsc-out
		cp ../../../external/foss/nso-tools* ../shared-jar/
		cp ../../../external/foss/guava* ../shared-jar/
		cp ../../../external/foss/commons-* ../shared-jar/

postyang:
	@if [ "$(YANG_GEN)" ]; then \
		rm -rf ./java/src/com/singtel/nfv/model; \
		$(YANG_GEN) \
			-C java-rw \
			-P vcpe,?ncs \
			-x com.singtel.nfv.model \
			-B com.cisco.singtel.resourcemanager.base \
			-d ./java/src ../../Sites/src/yang/ $(NCS_DIR)/src/ncs/yang; \
	fi

javac:
	cd java && ant -q all

clean:
	rm -rf  ../load-dir/* java/src/$(JDIR)/namespaces
	rm -f ../*jar/*.jar
	cd java && ant -q clean || true
	rm -f java/src/$(JDIR)/$(NS)/*.java

