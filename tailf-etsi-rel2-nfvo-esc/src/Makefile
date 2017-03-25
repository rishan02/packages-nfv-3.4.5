JAVA_PACKAGE = com.tailf.nfvo.esc
JDIR = $(shell echo $(JAVA_PACKAGE) | sed 's/\./\//g')

NS  = namespaces

JFLAGS = --java-disable-prefix \
         --exclude-enums \
         --fail-on-warnings \
         --java-package $(JAVA_PACKAGE).$(NS) \
         --emit-java java/src/$(JDIR)/namespaces

all:	fxs javac

# Include standard NCS examples build definitions and rules
include $(NCS_DIR)/src/ncs/build/include.ncs.mk

DEPS=../..

SRC = $(wildcard yang/*.yang)
FXS = ../load-dir/tailf-etsi-rel2-nfvo-esc.fxs ../load-dir/tailf-etsi-rel2-nfvo-esc-staged-delete.fxs
YANGPATH = \
		 --yangpath yang \
	   --yangpath $(DEPS)/tailf-etsi-rel2-nfvo/src/yang \
	   --yangpath $(DEPS)/esc/src/ncsc-out/modules/yang

fxs:	$(FXS)

../load-dir/%.fxs:yang/%.yang
	@echo "$(JDIR)"
	$(NCSC)  `ls yang/$*-ann.yang  > /dev/null 2>&1 && echo "-a yang/$*-ann.yang"` \
             $(YANGPATH) -c -o $@ $<
	$(NCSC) $(JFLAGS)/$*.java $@

javac:
	cd java && ant -q all


clean:
	rm -rf  ../load-dir/*
	rm -rf  ./src/*/*.pyc
