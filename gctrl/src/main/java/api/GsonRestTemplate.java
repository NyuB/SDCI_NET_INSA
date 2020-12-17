package api;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.GsonHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;

public class GsonRestTemplate extends RestTemplate {
	public GsonRestTemplate() {
		super(Arrays.asList(new HttpMessageConverter<?>[]{new GsonHttpMessageConverter()}));
	}

	public <T> T putForObject(String url, Object request, Class<T> responseClass) {
		return exchange(url, HttpMethod.PUT, new HttpEntity<>(request), responseClass).getBody();
	}
}
