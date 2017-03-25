all: doc

# Include standard NCS examples build definitions and rules
include $(NCS_DIR)/src/ncs/build/include.ncs.mk

GEN := $(shell mktemp)

SRC = $(wildcard yang/utm.yang)
YANG_SRC = $(wildcard yang/*yang)
PNG_SRC = $(wildcard ../doc/src/*.uml)

TREE_DOC = $(SRC:yang/%.yang=../doc/%.tree)
HTML_DOC = $(SRC:yang/%.yang=../doc/%.html)
YANGPATH = $(NCS_DIR)/src/ncs/yang/:./yang/:../../Sites/src/yang
PNG_DOC := $(YANG_SRC:yang/%.yang=../doc/%.png)
PNG2_DOC = $(PNG_SRC:../doc/src/%.uml=../doc/uml/%.png)

YANGPATH = $(NCS_DIR)/src/ncs/yang/:./yang/:../../Sites/src/yang/

doc:  $(PNG2_DOC) $(TREE_DOC) $(HTML_DOC) $(PNG_DOC)

../doc/uml/%.png: ../doc/src/%.uml ../../../external/foss/plantuml.jar
	java -Djava.awt.headless=true -jar ../../../external/foss/plantuml.jar -o "../uml/" $<


../doc/%.tree:	yang/%.yang
	mkdir -p ../doc
	pyang -f tree  -p $(YANGPATH) $< -o $@

../doc/%.html:	yang/%.yang
	mkdir -p ../doc
	pyang -f html  -p $(YANGPATH) $< -o $@

../doc/%.png:	yang/%.yang
	pyang -f uml  -p $(YANGPATH) --uml-inline-groupings  --uml-output-directory=. $< -o $(GEN)
	java -Djava.awt.headless=true -jar ../../../external/foss/plantuml.jar -o "$(PWD)/../doc/" $(GEN)


clean:
	rm -rf   ../doc/*

