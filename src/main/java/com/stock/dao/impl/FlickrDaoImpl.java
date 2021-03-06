package com.stock.dao.impl;

import java.io.InputStream;

import javax.swing.JOptionPane;

import com.flickr4java.flickr.Flickr;
import com.flickr4java.flickr.REST;
import com.flickr4java.flickr.RequestContext;
import com.flickr4java.flickr.auth.Auth;
import com.flickr4java.flickr.auth.AuthInterface;
import com.flickr4java.flickr.auth.Permission;
import com.flickr4java.flickr.uploader.UploadMetaData;
import com.github.scribejava.core.model.OAuth1RequestToken;
import com.github.scribejava.core.model.OAuth1Token;
import com.stock.dao.IFlickrDao;


public class FlickrDaoImpl implements IFlickrDao {
	
	private Flickr flick;
	
	private UploadMetaData data = new UploadMetaData();
	
	private String apiKey = "f74e0f5bc25f9e0204c90b671580c10c";
	
	private String sharedSecret = "f14434e07b44b9b0";
	
	private static final String token = "72157711614887766-38ec13de10bd3a1d";
	
	private static final String secret = "0211f9b73675c171";
	
	private void auth() {
		flick = new Flickr(apiKey, sharedSecret, new REST());
		Auth auth= new Auth();
		auth.setPermission(Permission.READ);
		auth.setToken(token);
		auth.setTokenSecret(secret);
		RequestContext context = RequestContext.getRequestContext();
		context.setAuth(auth);
		flick.setAuth(auth);
	}
	

	@Override
	public String upload(InputStream stream, String fileName) throws Exception {
		this.auth();
		data.setTitle(fileName);
		String id = flick.getUploader().upload(stream, data);
		return flick.getPhotosInterface().getPhoto(id).getMedium640Url();
	}
	
	public void getToken() {
		Flickr flickr = new Flickr(apiKey, sharedSecret, new REST());
		AuthInterface authInterface = flickr.getAuthInterface();
		OAuth1RequestToken token = authInterface.getRequestToken();
		
		System.out.println("Token: "+token);
		
		String url = authInterface.getAuthorizationUrl(token, Permission.DELETE);
		System.out.println("***Generation du token***");
		System.out.println("Ulr: "+url);
		
		String tokenKey = JOptionPane.showInputDialog(null);
		
		OAuth1Token requestToken = authInterface.getAccessToken(token, tokenKey);
		
		Auth auth = null;
		try {
			auth = authInterface.checkToken(requestToken);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		System.out.println("Token: "+requestToken.getToken());
		System.out.println("Secret: "+requestToken.getTokenSecret());
		System.out.println("usid: "+auth.getUser().getId());
		System.out.println("RealName: "+auth.getUser().getRealName());
		System.out.println("Username: "+auth.getUser().getUsername());
		System.out.println("Permission: " + auth.getPermission().getType());
	}

}
