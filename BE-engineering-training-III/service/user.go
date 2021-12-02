package service

import (
	"tickethub_service/apimodel/request"
	"tickethub_service/apimodel/response"
	"tickethub_service/model"
	"tickethub_service/util"
	"tickethub_service/util/enum"
	"tickethub_service/util/log"
	"time"
)

func GetUser(req request.GetUserRequest) (response.GetUserResponse, error) {
	var resp response.GetUserResponse
	user, err := model.GetUser(req.ID)
	if err != nil {
		log.Errorf("Failed to get user by id[%v] %v:", req.ID, err)
		return resp, err
	}

	resp.Load(user)
	return resp, nil
}

func CreateUser(req request.CreateUserRequest) (response.CreateUserResponse, error) {
	var resp response.CreateUserResponse
	user := model.User{
		ID:          util.NewUUIDString(enum.TABLENAME_USER),
		Nickname:    req.Nickname,
		Avatar:      req.Avatar,
		Telephone:   req.Telephone,
		Description: req.Description,
		CreateTime:  time.Now(),
		UpdateTime:  time.Now(),
	}

	newUser, err := CreateUserWithZjpay(user)
	if err != nil {
		log.Errorf("Failed to create UserWithZjpay: %v", err)
		return resp, err
	}
	resp.Load(newUser)
	return resp, nil
}

func UpdateUser(req request.UpdateUserRequest) error {
	user, err := GetUserByID(req.ID)
	if err != nil {
		log.Errorf("Failed to get user by id[%v] %v:", req.ID, err)
		return err
	}

	//TODO check payID
	user.PayID = req.PayID
	user.Nickname = req.Nickname
	user.Avatar = req.Avatar
	user.Telephone = req.Telephone
	user.Description = req.Description
	user.UpdateTime = time.Now()
	err = model.UpdateUser(user)
	if err != nil {
		log.Errorf("Failed to UpdateUser in database req[%v]:%v", req, err)
		return err
	}
	if req.Nickname != "" {
		err := model.UpdateOrder(model.Order{UserID: user.ID}, model.Order{UserName: user.Nickname})
		if err != nil {
			log.Errorf("Failed to update order:%v", err)
			return err
		}

		err = model.UpdateComment(model.Comment{UserID: user.ID}, model.Comment{UserName: user.Nickname})
		if err != nil {
			log.Errorf("Failed to update comment: %v", err)
			return err
		}
	}
	return nil
}

func DeleteUser(req request.DeleteUserRequest) error {
	err := model.DeleteUser(req.ID)
	if err != nil {
		log.Errorf("Failed to DeleteUser in database req[%v]: %v", req, err)
		return err
	}

	return nil
}

func CreateUserWithZjpay(user model.User) (model.User, error) {
	zjpay, err := CreateZjPay()
	if err != nil {
		log.Errorf("Failed to create Zjpay : %v", err)
		return model.User{}, err
	}
	user.PayID = zjpay.ID
	newUser, err := model.CreateUser(user)
	if err != nil {
		log.Errorf("Failed to create user[%v]: %v", user, err)
		return model.User{}, err
	}
	return newUser, nil
}

func GetUserByID(ID string) (model.User, error) {
	return model.GetUser(ID)
}
