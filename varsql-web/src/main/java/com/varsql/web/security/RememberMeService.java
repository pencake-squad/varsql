package com.varsql.web.security;

import java.util.Date;

import org.springframework.security.web.authentication.rememberme.PersistentRememberMeToken;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.stereotype.Service;

import com.varsql.web.model.entity.user.RememberMeEntity;
import com.varsql.web.security.repository.RememberMeEntityRepository;

@Service
public class RememberMeService implements PersistentTokenRepository {
	
	private RememberMeEntityRepository rememberMeEntityRepository;
	
	@Override
	public void createNewToken(PersistentRememberMeToken token) {
		RememberMeEntity me = RememberMeEntity.builder()
				.series(token.getSeries())
				.token(token.getTokenValue())
				.username(token.getUsername())
				.lastUsed(token.getDate())
				.build();
		
		rememberMeEntityRepository.save(me);
	}

	@Override
	public void updateToken(String series, String tokenValue, Date lastUsed) {
		rememberMeEntityRepository.updateToken(series, tokenValue, lastUsed);
	}

	@Override
	public PersistentRememberMeToken getTokenForSeries(String seriesId) {
		RememberMeEntity rm = rememberMeEntityRepository.findBySeries(seriesId);
		if(rm != null) {
			return new PersistentRememberMeToken(rm.getUsername(), rm.getSeries(), rm.getToken(), rm.getLastUsed());
		} else {
			return null;
		}
	}

	@Override
	public void removeUserTokens(String username) {
		rememberMeEntityRepository.deleteByUsername(username);
	}
}