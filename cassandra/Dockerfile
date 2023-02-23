FROM cassandra:3.11.9

COPY custom-docker-entrypoint.sh /custom-docker-entrypoint.sh
ENTRYPOINT ["/custom-docker-entrypoint.sh"]
CMD ["cassandra", "-f"]