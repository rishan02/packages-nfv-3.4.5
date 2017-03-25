all: fxs

# Include standard NCS examples build definitions and rules
include $(NCS_DIR)/src/ncs/build/include.ncs.mk

SRC = yang/tailf-etsi-rel2-nfvo.yang
SUBMODULES=yang/tailf-etsi-rel2-nfvo-vnf.yang yang/tailf-etsi-rel2-nfvo-ns.yang yang/tailf-etsi-rel2-nfvo-common.yang
FXS = $(SRC:yang/%.yang=../load-dir/%.fxs)

YANGPATH ?= --yangpath yang
## Uncomment and patch the line below if you have a dependency to a NED
# YANGPATH += ../../<ned-name>/src/ncsc-out/modules

.PHONY: fxs
fxs: $(FXS)

../load-dir/%.fxs: yang/%.yang $(SUBMODULES)
	$(NCSC)  `ls $*-ann.yang  > /dev/null 2>&1 && echo "-a $*-ann.yang"` \
             $(YANGPATH) -c -o $@ $<
clean:
	rm -rf  ../load-dir/*
