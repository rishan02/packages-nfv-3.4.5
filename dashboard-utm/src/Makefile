all: postyang javac
.PHONY: all

# Include standard NCS examples build definitions and rules
include $(NCS_DIR)/src/ncs/build/include.ncs.mk

JAVA_PACKAGE = com.cisco.singtel.dashboardUtm
JDIR = $(shell echo $(JAVA_PACKAGE) | sed 's/\./\//g')

NS  = namespaces

JFLAGS = --java-disable-prefix \
         --exclude-enums \
         --fail-on-warnings \
         --java-package $(JAVA_PACKAGE).$(NS) \
         --emit-java java/src/$(JDIR)/namespaces

SRC = $(wildcard yang/*.yang)
DIRS = ../load-dir ../shared-jar ../private-jar ncsc-out java/src/$(JDIR)/$(NS)
FXS = $(SRC:yang/%.yang=../load-dir/%.fxs)

postyang:
	@if [ "$(YANG_GEN)" ]; then \
		rm -rf ./java/src/com/cisco/singtel/utm/model; \
		rm -rf ./java/src/com/cisc/singtel/utm/types/model; \
		$(YANG_GEN) \
			-C java-rw \
			-P db,dbutm,?ncs \
			-x com.cisco.singtel.dashboardUtm.model \
			-X ut:com.cisco.singtel.dashboardUtm.types.model \
			-B com.cisco.singtel.resourcemanager.base \
			-d ./java/src \
			../../dashboard/src/yang/ ./yang/ $(NCS_DIR)/src/ncs/yang; \
	fi



javac: $(DIRS) fxs
	cd java && ant -q all
.PHONY: javac

fxs: $(FXS)
.PHONY: fxs

$(DIRS):
	mkdir -p $@

../load-dir/utm-dashboard.fxs:	yang/utm-dashboard.yang
	$(NCSC) --yangpath ./yang/:../../dashboard/src/yang/ -c -o $@ $<
	$(NCSC) $(JFLAGS)/utmDashboard.java $@

clean:
	rm -rf  ../load-dir/* java/src/$(JDIR)/namespaces
	rm -f ../*jar/*.jar
	cd java && ant -q clean || true
	rm -f java/src/$(JDIR)/$(NS)/*.java

.PHONY: clean
