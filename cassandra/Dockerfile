FROM cassandra:4.1.0

COPY custom-docker-entrypoint.sh /custom-docker-entrypoint.sh
ENTRYPOINT ["/custom-docker-entrypoint.sh"]
CMD ["cassandra", "-f"]