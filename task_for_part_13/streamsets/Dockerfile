FROM streamsets/datacollector:3.17.0
USER root
RUN mkdir -p /opt/ext/streamsets-datacollector-jdbc-lib/lib
ENV STREAMSETS_LIBRARIES_EXTRA_DIR="/opt/ext"
RUN $SDC_DIST/bin/streamsets stagelibs -install=streamsets-datacollector-jdbc-lib
#ADD https://repo1.maven.org/maven2/org/mariadb/jdbc/mariadb-java-client/2.6.0/mariadb-java-client-2.6.0.jar /opt/ext/streamsets-datacollector-jdbc-lib/lib/mariadb-java-client-2.6.0.jar
#COPY custom.policy /tmp/custom.policy
#RUN cat  /tmp/custom.policy >> /etc/sdc/sdc-security.policy
#RUN chown -R sdc:sdc /opt/ext
#USER sdc