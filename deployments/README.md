# Sonar Deployment
How to deploy new SonarQube and restore from backup.

SonarQube has two main components: database and UI (including API).
SonarQube supports many databases for backend but we use
PostgreSQL. The UI is a stateless program and the only important part of it
is the server side configuration including database connection settings.
The production environment has the two components separated into
different hosts.

Our automation allows you to deploy both single host and separated host
deployment. A single host is more suited for testing and development.
The automation is Ansible based, developed for RHEL, and maintained on GitHub:

 - [ansible-sonar-deployment][2]
 - [ansible-role-sonar][3]

On production UI server there is reverse proxy Nginx
[installed manually][1]. With the following configuration
(/etc/nginx/nginx.conf):

```
user  nginx;
worker_processes  1;

error_log  /var/log/nginx/error.log warn;
pid        /var/run/nginx.pid;


events {
    worker_connections  1024;
}


http {
    include       /etc/nginx/mime.types;
    default_type  application/octet-stream;

    log_format  main  '$remote_addr - $remote_user [$time_local] "$request" '
                      '$status $body_bytes_sent "$http_referer" '
                      '"$http_user_agent" "$http_x_forwarded_for"';

    access_log  /var/log/nginx/access.log  main;

    sendfile        on;
    #tcp_nopush     on;

    keepalive_timeout  65;

    #gzip  on;

    include /etc/nginx/conf.d/*.conf;

    # Can't push large reports to SonarQube server
    # 413 Request Entity Too Large
    client_max_body_size 100M;
}
```

## How to run automation

Requirements:

 - Ansible

Steps:

1. Setup:

  ```bash
  git clone https://github.com/shakedlokits/CodeQuality.git
  cd CodeQuality/deployments/ansible-sonar-deployment
  ansible-galaxy install -r requirements.yml -p roles
  ```

2. Edit 'inventory' file:

  - Separated UI and database:

    ```
    db ansible_host=<db_address> ansible_ssh_user=root
    ui ansible_host=<ui_address> ansible_ssh_user=root
    ```

  - Single host:

    ```
    db ansible_host=<sonar_address> ansible_ssh_user=root
    ui ansible_host=<sonar_address> ansible_ssh_user=root
    ```

3. Run automation:
  - Clean deployment:

    ```bash
    ansible-playbook -i inventory playbook.yml
    ```
  - Restore from backup:

    ```bash
    ansible-playbook -i inventory playbook.yml --extra-vars "sonar_db_backup=</path/to/sonar/db/dump>"
    ```

Notes:
 - When the automation is finished, you can browse to the UI on port 9000.
 - To have SonarQube listen on port 80, you will need to setup reverse
   proxy, for example, Nginx.
 - If you restored SonarQube from backup and don't know the admin password
   then SSH to database system, and then:

   ```
   su - postgres
   psql
   update users set crypted_password = '88c991e39bb88b94178123a849606905ebf440f5', salt='6522f3c5007ae910ad690bb1bdbf264a34884c6d' where login = 'admin';
   ```

 - SonarQube settings are found on the UI server at `/usr/local/sonar/conf`.
   You will need it if you have issues with database connection or plugin
   configuration.

[1]: https://www.cyberciti.biz/faq/how-to-install-and-use-nginx-on-centos-7-rhel-7/
[2]: ansible-sonar-deployment
[3]: https://github.com/abraverm/ansible-role-sonar/tree/postgresql
