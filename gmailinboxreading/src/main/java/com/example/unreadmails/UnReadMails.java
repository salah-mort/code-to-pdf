package com.example.unreadmails;

import java.util.List;

import javax.mail.AuthenticationFailedException;
import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.FolderNotFoundException;
import javax.mail.Message;
import javax.mail.NoSuchProviderException;
import javax.mail.Store;
import javax.mail.search.FlagTerm;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.common.AuthenticationData;
import com.example.common.CommonCode;
import com.example.common.InboxData;

/**
 * @author Shivaji Chandra
 *
 */
@RestController
public class UnReadMails {

	
	/**
	 * @param data
	 * @return
	 * @throws AuthenticationFailedException
	 * @throws FolderNotFoundException
	 * @throws IllegalStateException
	 * @throws NoSuchProviderException
	 * @throws Exception
	 */
	@PostMapping("/unread-messages")
	public List<InboxData> unreadMails(@RequestBody AuthenticationData data) throws AuthenticationFailedException, FolderNotFoundException,
	IllegalStateException, NoSuchProviderException, Exception{
		
		if(data.getUsername() == null || data.getPassword() == null) {
			throw new Exception("Required username and password");
		}
		
		Store emailStore = CommonCode.setPropertyAndAuthenticateUser(data.getUsername(), data.getPassword());

		// getting the email folder by folder name
		Folder emailFolder = emailStore.getFolder("INBOX");

		// setting the accessed folder as READ_ONLY
		emailFolder.open(Folder.READ_ONLY);

		// getting all the messages from the particular folder name
		Message[] messages = emailFolder.search(new FlagTerm(new Flags(Flags.Flag.SEEN), false));

		// returning all the inbox data
		return CommonCode.inboxData(messages);
	}
}
