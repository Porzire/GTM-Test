PROJECTS_DIR=~/projects
PROJECT_NAME=GTM-Test
PROJECT_ROOT=~/projects/$(PROJECT_NAME)
RESOURCE_DIR=$(PROJECT_ROOT)/src/main/resources
TARGET_DIR=$(PROJECT_ROOT)/target

default:
	echo "Please select a task: backup, revert, deploy"

backup: $(PROJECT_ROOT).backup
	rsync -arv --exclude=src/main/resources --exclude=target $(PROJECT_ROOT) $(PROJECTS_DIR)/$(PROJECT_NAME).backup

$(PROJECT_ROOT).backup:
	mkdir -p $@

revert:
	rsync -arv --exclude=src/main/resources --exclude=target $(PROJECTS_DIR)/$(PROJECT_NAME).backup $(PROJECT_ROOT)

deploy:
	scp -r $(PROJECT_ROOT) jie@cgm6.research.cs.dal.ca:~
