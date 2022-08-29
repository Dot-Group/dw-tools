Dropwizard Redis Bundle
======================

An addon bundle for using the excellent Jedis redis client in Dropwizard applications.


Dependency Info
---------------

```xml
<dependency>
	<groupId>ai.dot.dwtools</groupId>
	<artifactId>dw-redis</artifactId>
	<version>1.0.0-x</version>
</dependency>
```


Usage
-----

Add a `JedisFactory` class:

```java
@NotNull
@JsonProperty
private JedisFactory redis;

public JedisFactory getJedisFactory() {
	return redis;
}

public void setJedisFactory(JedisFactory jedisFactory) {
	this.redis = jedisFactory;
}
```

Add a `JedisBundle` class

```java
@Override
public void initialize(Bootstrap<MyConfiguration> bootstrap) {
    // ...
    bootstrap.addBundle(new JedisBundle<AppConfig>() {
        @Override
        public JedisFactory getJedisFactory(AppConfig configuration) {
            return configuration.getJedisFactory();
        }
    });
}
```

This will enable `@Context` injection of pooled [Jedis] and [JedisPool] in resource methods:

```java
@GET
@Path("/posts/{id}")
public BlogPost getPost(@QueryParam("id") int postId, @Context Jedis jedis) {
  String cachedBlogContent = jedis.get("post-" + postId);
  // do stuff
  // No need to close the connection, it happens automatically.
}
```

This will also enable redis health-checking.


Configuration
-------------

For configuration the redis connection, there is `JedisFactory`:

```yaml
redis:
  # The redis server's address; required.
  endpoint: localhost:6379
  # Auth password for redis server connection.  (default: null)
  password: null
  # The minimum number of idle connections to maintain in the pool.  (default: 0)
  minIdle: 0
  # The maximum number of idle connections allowed in the pool.  (default: 0)
  maxIdle: 0
  # The maximum number of connections allowed in the pool.  (default: 1024)
  maxTotal: 1924
  # Enables SSL connection. (default: false)
  ssl: false
  # The configured timeout (in milliseconds) for redis connections in the connection pool.  (default: 2000)
  timeout: 2000
```
