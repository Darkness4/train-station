package queryargs

type GetMany struct {
	Search string `query:"s"`
	Limit  string `query:"limit"`
	Page   string `query:"page"`
}

type SearchLibelle struct {
	Libelle ContainType `json:"libelle"`
}

type ContainType struct {
	Contain string `json:"$cont"`
}
