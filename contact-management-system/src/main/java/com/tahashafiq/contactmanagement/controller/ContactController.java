package com.tahashafiq.contactmanagement.controller;
import com.tahashafiq.contactmanagement.dto.PostContactDto;
import com.tahashafiq.contactmanagement.entity.ContactEntity;
import com.tahashafiq.contactmanagement.impl.ContactServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/contacts")
public class ContactController {
    @Autowired
    private ContactServiceImpl contactService;
@GetMapping
    public ResponseEntity<List<ContactEntity>> getAllContacts() {
        return ResponseEntity.ok(contactService.findAllContact());
    }

    @GetMapping("/getContactById/{contactId}")
    public  ResponseEntity<ContactEntity> getContactById(@PathVariable String contactId){
        return ResponseEntity.ok(contactService.getContactById(contactId));
    }

    @GetMapping("/getContact/{userName}")
    public ResponseEntity<List<ContactEntity>> getContactByUserName(@PathVariable String userName){
        return ResponseEntity.ok(contactService.findContactsByUserName(userName));
    }

    @PostMapping("/createContact/{userName}")
    public ResponseEntity<ContactEntity>
    createContact(
            @RequestBody PostContactDto contactDto,
            @PathVariable  String userName
    ){
        return ResponseEntity.ok(contactService.createContact(contactDto,userName));
    }


    @PutMapping("/changeContact/{userName}/{contactId}")
    public ResponseEntity<ContactEntity> changeContact(
            @PathVariable String userName,
            @PathVariable String contactId,
            @RequestBody PostContactDto contactDto){
        ContactEntity contactById = contactService.getContactById(contactId);
        if(contactById != null) {
            if(contactById.getUserName().equals(userName)) {
                if(contactDto.getPhoneNumber()!=null && !contactDto.getPhoneNumber().isBlank()){
                    contactById.setPhoneNumber(contactDto.getPhoneNumber());
                }
            }
        }
        return ResponseEntity.ok(contactService.updateContact(contactById));
    }
}
