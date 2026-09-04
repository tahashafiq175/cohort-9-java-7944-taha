package com.tahashafiq.contactmanagement.impl;

import com.tahashafiq.contactmanagement.exceptionhandling.ResourceNotFoundException;

import com.tahashafiq.contactmanagement.dto.PostContactDto;
import com.tahashafiq.contactmanagement.entity.ContactEntity;
import com.tahashafiq.contactmanagement.entity.UserEntity;
import com.tahashafiq.contactmanagement.repository.ContactRepository;
import com.tahashafiq.contactmanagement.service.ContactService;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;


@Service
@Slf4j
public class ContactServiceImpl implements ContactService {


    private final ContactRepository contactRepository;
    private final UserServiceImpl userService;

    ContactServiceImpl(ContactRepository contactRepository, UserServiceImpl userService) {
        this.contactRepository = contactRepository;
        this.userService = userService;
    }

    @Override
    public List<ContactEntity> findAllContact() {
        return contactRepository.findAll();
    }

    @Override
    public List<ContactEntity> findAllContactOfUser(String userName){
        UserEntity entityByUserName = userService.findEntityByUserName(userName);
        return entityByUserName.getContactEntities();
    }

    @Override
    public ContactEntity getContactById(String contactId) {

        return contactRepository.findById(contactId).
                orElseThrow(()-> new ResourceNotFoundException("reource not found"));
    }

    @Override
    public ContactEntity createContact(PostContactDto contactEntityDto, String userName) {
        ContactEntity contactEntity = mapToContactDto(contactEntityDto);
           UserEntity userEntity= userService.findEntityByUserName(userName);
           contactEntity.setUserEntity(userEntity);
           userEntity.getContactEntities().add(contactEntity);
           userService.saveUser(userEntity);
        return contactEntity;
    }
    @Override
    public ContactEntity updateContact(PostContactDto updatedContact, ContactEntity existedContact) {
        if(updatedContact.getPhoneNumber()!=null && !updatedContact.getPhoneNumber().isEmpty()){
            existedContact.setPhoneNumber(updatedContact.getPhoneNumber());
        }
        if(updatedContact.getUserName()!=null && !updatedContact.getUserName().isEmpty()){
            existedContact.setUserName(updatedContact.getUserName());
        }
        if(updatedContact.getPhoneLabel()!=null && !updatedContact.getPhoneLabel().isEmpty()){
            existedContact.setPhoneLabel(updatedContact.getPhoneLabel());
        }
       return contactRepository.save(existedContact);
    }

    @Override
    public void deleteContactById(ContactEntity contactEntity) {


        contactRepository.delete(contactEntity);


        log.info("Contact with details {} deleted successfully", contactEntity);

    }

    @Override
    public List<ContactEntity> findContactsByUserName(String userName) {
        List<ContactEntity> byUserName = contactRepository.findAllByUserName(userName);
        if(byUserName==null){
            throw new ResourceNotFoundException("resource not found");
        }
        return byUserName;
    }

    ContactEntity mapToContactDto(PostContactDto contactDto) {
        ContactEntity contactEntity = new ContactEntity();
        contactEntity.setContactId(UUID.randomUUID().toString());
        contactEntity.setPhoneNumber(contactDto.getPhoneNumber());
        contactEntity.setUserName(contactDto.getUserName());
        return  contactEntity;
    }
}
