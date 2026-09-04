package com.tahashafiq.contactmanagement.controller;
import com.tahashafiq.contactmanagement.exceptionhandling.ResourceNotFoundException;
import com.tahashafiq.contactmanagement.dto.PostContactDto;
import com.tahashafiq.contactmanagement.entity.ContactEntity;
import com.tahashafiq.contactmanagement.impl.ContactServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/contacts")
@Slf4j
@Tag(name="Contact Apis")
public class ContactController {
    private final ContactServiceImpl contactService;

    @Autowired
    public ContactController(ContactServiceImpl contactService) {
        this.contactService = contactService;
    }
    @GetMapping("/getContactOfUser")
    @Operation(summary = "get all the contact of User")
    @ApiResponse(responseCode = "200", description = "Contacts retrieved successfully")
    @ApiResponse(responseCode = "401", description = "Unauthorized")

    public ResponseEntity<List<ContactEntity>> getAllContactOfUser(Authentication authentication) {
        String userName = authentication.getName();
        return ResponseEntity.ok(contactService.findAllContactOfUser(userName));
    }

    @GetMapping("/getContactById/{contactId}")
    @Parameter(description = "ContactId")
    @Operation(summary = "get the contact by ContactId")
    @ApiResponse(responseCode = "200", description = "Contact retrieved successfully")
    @ApiResponse(responseCode = "401", description = "Unauthorized")
    public  ResponseEntity<ContactEntity> getContactById(@PathVariable String contactId){
        return ResponseEntity.ok(contactService.getContactById(contactId));
    }


    @PostMapping("/createContact")
    @Operation(summary = "Creating the Contact corresponding to Particular user")
    @ApiResponse(responseCode = "201", description = "Journal created successfully")
    @ApiResponse(responseCode = "400", description = "Invalid journal data")
    @ApiResponse(responseCode = "401", description = "Unauthorized")
    public ResponseEntity<ContactEntity>
    createContact(
            @RequestBody PostContactDto contactDto,
            Authentication authentication
    ){
        String userName=authentication.getName();
        return ResponseEntity.ok(contactService.createContact(contactDto,userName));
    }


    @PutMapping("/changeContact/{contactId}")
    @Parameter(description = "ContactId")
    @Operation(summary = "Update a Contact entry")
    @ApiResponse(responseCode = "200", description = "Contact updated successfully")
    @ApiResponse(responseCode = "404", description = "Contact not found")
    @ApiResponse(responseCode = "400", description = "Invalid Contact data")
    @ApiResponse(responseCode = "401", description = "Unauthorized")
    public ResponseEntity<ContactEntity> changeContact(
            @PathVariable String contactId,
            @RequestBody PostContactDto contactDto,
            Authentication authentication){
        String authUserName = authentication.getName();
        ContactEntity contactById = contactService.getContactById(contactId);
        String userName1 = contactById.getUserEntity().getUserName();
        if(!userName1.equals(authUserName)){
            if (log.isErrorEnabled()) {
                log.error("username not matched");
            }
            throw new ResourceNotFoundException("the UserName corresponding to the Contact Entity and The One Received through the authenictation are not matched ");
        }
        return ResponseEntity.ok(contactService.updateContact(contactDto,contactById));
    }


    @DeleteMapping("/deleteContact/{contactId}")
    @Operation(summary = "Delete the Contact by Id")
    @Parameter(description = "ContactId")
    @ApiResponse(responseCode = "204", description = "Contact deleted successfully")
    @ApiResponse(responseCode = "404", description = "Contact not found")
    @ApiResponse(responseCode = "401", description = "Unauthorized")
    public ResponseEntity<ContactEntity> deleteContact(
            @PathVariable String contactId,
            Authentication authentication){
        String userName = authentication.getName();
        ContactEntity contactById = contactService.getContactById(contactId);
        String userName1 = contactById.getUserEntity().getUserName();
        if(!userName1.equals(userName)){
            log.error("username corresponding to the searched one does not matched");
            throw new ResourceNotFoundException("username not matched");
        }

        contactService.deleteContactById(contactById);
        return ResponseEntity.noContent().build();

    }
}
