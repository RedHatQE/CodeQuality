FROM fedora:latest
MAINTAINER Shaked Lokits "shaked.lokits@gmail.com"


###############################################################################
############################ Initial Installations ############################
###############################################################################

RUN dnf -y update
RUN dnf -y install openssh-server git java-1.8.0-openjdk.x86_64 which


###############################################################################
############################### Setup SSH Server ##############################
###############################################################################

# setup ssh server and password
RUN mkdir -p /var/run/sshd
RUN echo "root:password" | chpasswd
RUN ssh-keygen -A -t rsa -f /etc/ssh/ssh_host_rsa_key -N ''
RUN sed -i 's|session    required     pam_loginuid.so|session\
            optional     pam_loginuid.so|g' /etc/pam.d/sshd


###############################################################################
###################### Ports, Command and Entry Points ########################
###############################################################################

# expose ssh and http ports
EXPOSE 22 80

# run ssh server
CMD [ "/usr/sbin/sshd" , "-D" ]
