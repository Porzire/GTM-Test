PROJECTS_DIR=~/projects
PROJECT_NAME=GTM-Test
PROJECT_ROOT=~/projects/$(PROJECT_NAME)
RESOURCE_DIR=$(PROJECT_ROOT)/src/main/resources
TARGET_DIR=$(PROJECT_ROOT)/target
TEST_SERVER=jie@cgm6.research.cs.dal.ca

.PHONY: default backup revert deploy update
.SUFFIXES: .java

default:
	echo "Please select a task: backup, revert, deploy"

backup: $(PROJECT_ROOT).backup
	rsync -arv --exclude=src/main/resources --exclude=target $(PROJECT_ROOT) $(PROJECTS_DIR)/$(PROJECT_NAME).backup

$(PROJECT_ROOT).backup:
	mkdir -p $@

revert:
	rsync -arv --exclude=src/main/resources --exclude=target $(PROJECTS_DIR)/$(PROJECT_NAME).backup $(PROJECT_ROOT)

deploy:
	scp -r $(PROJECT_ROOT) $(TEST_SERVER):~

downloadFromCGM6:
	@scp $(TEST_SERVER):~/GTM-Test/src/main/java/$(filter-out $@,$(MAKECMDGOALS)) \
			~/projects/GTM-Test/src/main/java/$(filter-out $@,$(MAKECMDGOALS))

uploadToCGM6:
	@scp ~/projects/GTM-Test/src/main/java/$(filter-out $@,$(MAKECMDGOALS)) \
			$(TEST_SERVER):~/GTM-Test/src/main/java/$(filter-out $@,$(MAKECMDGOALS))

%.java:
	@echo finish updating $@ from cgm6.
