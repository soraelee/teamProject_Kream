package com.kream.root.admin.service;

import com.kream.root.Login.model.UserListDTO;
import com.kream.root.admin.repository.AdminMemberRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminMemberService {
    @Autowired
    private AdminMemberRepository adminMemberRepository;


    public List<UserListDTO> getAllUsers() {
        return adminMemberRepository.findUserAll();
    }

    @Transactional//이렇게 따로 설정하면 우선권을 가져서 디폴트값 펄스가 존재
    public String join(UserListDTO user){
        //같은 이름의 중복 회원 확인
        validateDuplicateAdmin(user);
        //회원 정보 저장
        adminMemberRepository.save(user);

        return user.getUserId();
    }
    private void validateDuplicateAdmin(UserListDTO user) {
        //중복 시 EXCEPTIO
        List<UserListDTO> findAdmins = adminMemberRepository.findByName(user.getUserId());
        //동일한 이름을 가진 맴버 목록을가져온다.
        if(!findAdmins.isEmpty()){
            throw new IllegalStateException("이미 존재하는 회원입니다.");
        }//동일한 이름을 가진 맴버가 존재할 경우 throw를 통해 익셉션을 생성하고
        //새로운 익셉션을 생성한다.

    }


    //회원 단일 조회
//    @Transactional(readOnly = true)
    public UserListDTO findOne(Long ULID){
        return adminMemberRepository.findOne(ULID);

    }
    //정보 업데이트
    @Transactional
    public UserListDTO updateAdmin(Long id, UserListDTO user) {
        UserListDTO existingUser = adminMemberRepository.findOne(id);
        if (existingUser == null) {
            throw new IllegalStateException("해당 회원이 존재하지 않습니다.");
        }
        UserListDTO updatedUser = new UserListDTO.Builder()
                .ulid(existingUser.getUlid())
                .userId(user.getUserId())
                .userPw(user.getUserPw())
                .userName(user.getUsername())
                .age(user.getAge())
                .phone(user.getPhone())
                .email(user.getEmail())
                .profileUrl(user.getProfileUrl())
                .profileName(user.getProfileName())
                .userSize(user.getUserSize())
                .userBio(user.getUserBio())
                .receiveEmail(user.getReceiveEmail())
                .receiveMessage(user.getReceiveMessage())
                .blockedProfiles(user.getBlockedProfiles())
                .favoriteProducts(user.getFavoriteProducts())
                .roles(user.getRoles())
                .build();

        return adminMemberRepository.update(updatedUser);
    }
}
