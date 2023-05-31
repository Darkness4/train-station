package github

import "encoding/json"

func UnmarshalUser(data []byte) (User, error) {
	var r User
	err := json.Unmarshal(data, &r)
	return r, err
}

func (r *User) Marshal() ([]byte, error) {
	return json.Marshal(r)
}

type User struct {
	Login                   string `json:"login"`
	ID                      int64  `json:"id"`
	NodeID                  string `json:"node_id"`
	AvatarURL               string `json:"avatar_url"`
	GravatarID              string `json:"gravatar_id"`
	URL                     string `json:"url"`
	HTMLURL                 string `json:"html_url"`
	FollowersURL            string `json:"followers_url"`
	FollowingURL            string `json:"following_url"`
	GistsURL                string `json:"gists_url"`
	StarredURL              string `json:"starred_url"`
	SubscriptionsURL        string `json:"subscriptions_url"`
	OrganizationsURL        string `json:"organizations_url"`
	ReposURL                string `json:"repos_url"`
	EventsURL               string `json:"events_url"`
	ReceivedEventsURL       string `json:"received_events_url"`
	Type                    string `json:"type"`
	SiteAdmin               bool   `json:"site_admin"`
	Name                    string `json:"name"`
	Company                 string `json:"company"`
	Blog                    string `json:"blog"`
	Location                string `json:"location"`
	Email                   string `json:"email"`
	Hireable                bool   `json:"hireable"`
	Bio                     string `json:"bio"`
	TwitterUsername         string `json:"twitter_username"`
	PublicRepos             int64  `json:"public_repos"`
	PublicGists             int64  `json:"public_gists"`
	Followers               int64  `json:"followers"`
	Following               int64  `json:"following"`
	CreatedAt               string `json:"created_at"`
	UpdatedAt               string `json:"updated_at"`
	PrivateGists            int64  `json:"private_gists"`
	TotalPrivateRepos       int64  `json:"total_private_repos"`
	OwnedPrivateRepos       int64  `json:"owned_private_repos"`
	DiskUsage               int64  `json:"disk_usage"`
	Collaborators           int64  `json:"collaborators"`
	TwoFactorAuthentication bool   `json:"two_factor_authentication"`
	Plan                    Plan   `json:"plan"`
}

type Plan struct {
	Name          string `json:"name"`
	Space         int64  `json:"space"`
	PrivateRepos  int64  `json:"private_repos"`
	Collaborators int64  `json:"collaborators"`
}
