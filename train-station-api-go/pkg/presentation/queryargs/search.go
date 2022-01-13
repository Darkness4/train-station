package queryargs

type SearchLibelle struct {
	Libelle ContainType `json:"libelle"`
}

type ContainType struct {
	Contain string `json:"$cont"`
}
