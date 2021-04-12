package com.high.highblog.config;

import com.paypal.core.PayPalEnvironment;
import com.paypal.core.PayPalHttpClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PaypalConfig {
    private final ApplicationConfigProperties.Paypal paypalConfigProperties;

    public PaypalConfig(final ApplicationConfigProperties applicationConfigProperties) {
        this.paypalConfigProperties = applicationConfigProperties.getPaypal();
    }

    @Bean
    public PayPalHttpClient payPalHttpClient() {
        PayPalEnvironment payPalEnvironment;
        if (paypalConfigProperties.getEnvironment().equalsIgnoreCase("SANDBOX")) {
            payPalEnvironment = new PayPalEnvironment.Sandbox(paypalConfigProperties.getClientId(),
                                                              paypalConfigProperties.getClientSecret());
        } else {
            payPalEnvironment = new PayPalEnvironment.Live(paypalConfigProperties.getClientId(),
                                                           paypalConfigProperties.getClientSecret());
        }

        return new PayPalHttpClient(payPalEnvironment);
    }
}
