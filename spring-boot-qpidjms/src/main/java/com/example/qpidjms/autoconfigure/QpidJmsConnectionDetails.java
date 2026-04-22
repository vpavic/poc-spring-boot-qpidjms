package com.example.qpidjms.autoconfigure;

import java.net.URI;

import org.jspecify.annotations.Nullable;
import org.springframework.boot.autoconfigure.service.connection.ConnectionDetails;

public interface QpidJmsConnectionDetails extends ConnectionDetails {

	URI getBrokerUrl();

	@Nullable String getUsername();

	@Nullable String getPassword();

}
