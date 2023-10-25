package com.wex.transactionmanager.services;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URI;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wex.transactionmanager.models.ConvertedAmount;
import com.wex.transactionmanager.services.exceptions.ExchangeRateNotFoundException;

import jakarta.annotation.PostConstruct;

@Service
public class CurrencyConverterService {
	private List<String> currencies;
	private static final String BASE_URL = "https://api.fiscaldata.treasury.gov/services/api/fiscal_service/v1/accounting/od/rates_of_exchange?";
	private static final String FIELDS_PARAM = "fields=country_currency_desc,effective_date,exchange_rate&";
	private static final String CURRENCY_LIST_PARAM = "fields=country_currency_desc&page[number]=1&page[size]=1000";
	private final ObjectMapper objectMapper;
	int counter = 0;

	@Autowired
	RestTemplate client;

	public CurrencyConverterService() {
		this.objectMapper = new ObjectMapper();
		objectMapper.findAndRegisterModules();
	}

	@PostConstruct
	private void initializeCurrencies() {
		setCurrencies(fetchCountryCurrenciesList());
	}


	public List<String> fetchCountryCurrenciesList() {
		try {
			URI uri = UriComponentsBuilder.fromUriString(BASE_URL + CURRENCY_LIST_PARAM).build().toUri();
			ResponseEntity<String> response = client.getForEntity(uri, String.class);
			if (response.getStatusCode().is2xxSuccessful()) {
				String responseBody = response.getBody();
				JsonNode jsonNode = objectMapper.readTree(responseBody);
				List<String> currencyList = new ArrayList<>();
				if (jsonNode.has("data")) {
					jsonNode.get("data").forEach(country -> {
						if (country.has("country_currency_desc")) {
							currencyList.add(country.get("country_currency_desc").asText());
						}
					});
				}
				return currencyList;
			} 
		} catch (Exception e) {
			System.out.println(e);
		}
		return Collections.emptyList();
	}

	public ConvertedAmount convertCurrency(String currency, BigDecimal amount, LocalDate date) {
		String matchedCurrencies = findMatchingCurrencies(currency);
		if (matchedCurrencies.isEmpty()) throw new ExchangeRateNotFoundException();
		ConvertedAmount convertedAmount = new ConvertedAmount();
		ExchangeRateData exchangeRateData = fetchExchangeRate(matchedCurrencies, date);
		convertedAmount.setCurrency(exchangeRateData.getCurrency());
		convertedAmount.setEffectiveDate(exchangeRateData.getDate());
		convertedAmount.setExchangeRate(exchangeRateData.getExchangeRate());
		convertedAmount.setAmount(convertAmount(convertedAmount.getExchangeRate(), amount));
		return convertedAmount;
	}

	private String findMatchingCurrencies(String currency) {
		List<String> matchingCurrencies = getCurrencies().stream()
				.filter(c -> c.equalsIgnoreCase(currency))
				.collect(Collectors.toList());

		String matchedCurrencies = matchingCurrencies.stream().collect(Collectors.joining(","));
		return matchedCurrencies;
	}

	private BigDecimal convertAmount(BigDecimal amount, BigDecimal rate) {
		return amount.multiply(rate).setScale(2, RoundingMode.HALF_EVEN);
	}

	public ExchangeRateData fetchExchangeRate(String currency, LocalDate date) {
		StringBuilder sb = new StringBuilder();
		sb.append(BASE_URL).append(FIELDS_PARAM)
		.append("filter=country_currency_desc:in:(")
		.append(currency).append("),")
		.append("effective_date:gte:")
		.append(date.minusMonths(6)).append(",")
		.append("effective_date:lte:")
		.append(date)
		.append("&")
		.append("sort=-effective_date&")
		.append("page[number]=1&page[size]=1");

		URI uri = UriComponentsBuilder.fromUriString(sb.toString()).build().toUri();
		ResponseEntity<String> response = client.getForEntity(uri, String.class);

		try {
			JsonNode json = objectMapper.readTree(response.getBody());
			JsonNode data = json.path("data");
			if (data.isArray() && data.size() > 0) {
				ExchangeRateData exchangeRateData = objectMapper.convertValue(data.get(0), ExchangeRateData.class);
				return exchangeRateData;
			}
			throw new ExchangeRateNotFoundException();
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			throw new ExchangeRateNotFoundException();
		}

	}

	public List<String> getCurrencies() {
		return currencies;
	}

	public void setCurrencies(List<String> currencies) {
		this.currencies = currencies;
	}

	public static class ExchangeRateData {
		@JsonProperty(value = "effective_date")
		private LocalDate date;
		@JsonProperty(value = "exchange_rate")
		private BigDecimal exchangeRate;
		@JsonProperty(value = "country_currency_desc")
		private String currency;

		public BigDecimal getExchangeRate() {
			return exchangeRate;
		}
		public void setExchangeRate(BigDecimal exchangeRate) {
			this.exchangeRate = exchangeRate;
		}
		public String getCurrency() {
			return currency;
		}
		public void setCurrency(String currency) {
			this.currency = currency;
		}
		public LocalDate getDate() {
			return date;
		}
		public void setDate(LocalDate date) {
			this.date = date;
		}
	}
}
