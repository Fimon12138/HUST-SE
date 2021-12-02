package request

type GetAccountRequest struct {
	AccountName string `json:"accountName"`
}

type CreateAccountRequest struct {
	Name      string `json:"name" binding:"required"`
	Password  string `json:"password" binding:"required"`
	Type      string `json:"type" binding:"required"`
	RelatedID string `json:"relatedId" binding:"required"`
}

type UpdateAccountRequest struct {
	Name     string `json:"name" binding:"required"`
	Password string `json:"password" binding:"required"`
}

type DeleteAccountRequest struct {
	Name string `json:"name"`
}

type LogIn struct {
	Name     string `json:"name" binding:"required"`
	Password string `json:"password" binding:"required"`
}

type SignUp struct {
	Name      string `json:"name" binding:"required"`
	Password  string `json:"password" binding:"required"`
	Telephone string `json:"telephone" binding:"required"`
	Type      string `json:"type" binding:"required"`
}
