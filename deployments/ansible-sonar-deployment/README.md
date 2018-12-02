### Install required roles by:
```sh
   ansible-galaxy install -r ./roles/requirements.yml -p roles
```
 
### Edit the inventory file with your "ui" and "db" hosts. 


### Deploy your env by running:
```sh
   ansible-playbook -i inventory playbook.yml
```

# For Ansible-Tower

 -  Inventory

    - Create inventory of 2 hosts. The name of the hosts would be "db" and "ui".
    - Include the next extra_vars for every host:
    - ansible_host: {Enter your host here}
    - ansible_ssh_user: root 

 - Project

    - Create project for sonar and connect it to git repo


 - Job_Template

    - Create one under sonar project you have been created.
    - Choose playbook.yml file from sonar project.
    - choose inventory with the 2 hosts.
