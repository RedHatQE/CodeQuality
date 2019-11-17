# Save Sonar Configuration

## User Guide

```bash
git clone https://github.com/RedHatQE/CodeQuality.git
cd save_configuration/
```
Edit sonar_url variable in vars/external_vars.yml file with the sonar instance
you want to inspect.

```bash
ansible-playbook playbook.yml
Enter Sonar username:
Enter Sonar password:
```

This will create a sonar_conf file contain all the plugins, users and projects
on this Sonar instance.
