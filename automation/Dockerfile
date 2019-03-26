FROM quay.io/openshift/origin-jenkins-agent-base:v4.0.0
# Jenkins slave requirements
RUN yum makecache \
 && yum erase -y java-1.8.0-openjdk-headless \
 && yum -y install openssl java-1.8.0-openjdk
# you can override JNLP Java options: https://github.com/openshift/jenkins/blob/master/slave-base/contrib/bin/run-jnlp-client
# And disable SSL verification: https://stackoverflow.com/questions/4663147/is-there-a-java-setting-for-disabling-certificate-validation/4663241#4663241
ENTRYPOINT ["/usr/local/bin/run-jnlp-client"]
# General requirements                                                          
RUN yum install -y epel-release \
 && yum install -y gcc python-devel \
 && yum install -y python-pip \
 && yum install -y git
                           
# Python requirements                                                           
RUN yum install -y python-devel.x86_64 \
 && pip install coverage unittest2
                                                                                
# CPP requirements
RUN yum install -y gcc-c++ cppunit-devel.x86_64 cppunit.x86_64 unzip \
    wget python-devel \
 && pip install gcovr

# Java requirements                                                             
RUN yum install -y maven 

# Golang requirements
RUN yum install -y golang

# Ruby requirements
ENV RBENV_ROOT="/home/jenkins"
ENV PATH="/home/jenkins/versions/2.3.0/bin:$PATH"
RUN yum install -y rubygems \
 && yum install -y openssl-devel readline-devel zlib-devel \
 && git clone --depth 1 https://github.com/rbenv/rbenv.git ~/.rbenv \
 && cd ~/.rbenv && src/configure && make -C src && cd ~ \
 && git clone --depth 1 https://github.com/rbenv/ruby-build.git \
 && PREFIX=/usr/local ./ruby-build/install.sh \
 && ~/.rbenv/bin/rbenv install 2.3.0 \
 && export PATH="home/jenkins/versions/2.3.0/bin:$PATH" \
 && gem install bundler \
 && chmod -R go+wx /usr/local \
 && chmod -R go+wx /usr/bin \
 && chmod -R go+wx /home/jenkins
