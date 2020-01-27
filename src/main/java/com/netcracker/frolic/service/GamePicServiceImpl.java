package com.netcracker.frolic.service;

import com.netcracker.frolic.entity.GamePic;
import com.netcracker.frolic.repository.GamePicRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Transactional
@Service("jpaGamePicService")
public class GamePicServiceImpl implements GamePicService {
    @Autowired
    private GamePicRepo gamePicRepo;

    @Transactional(readOnly = true)
    public Optional<GamePic> findById(long id) {
        return gamePicRepo.findById(id);
    }

    public void deleteById(long id) {
        gamePicRepo.deleteById(id);
    }

    public GamePic save(GamePic gamePic)
    { return gamePicRepo.save(gamePic); }
}
