import requests
import sys
import json

GraphQL_URL = "https://api.github.com/graphql"

if len(sys.argv) == 1:
	print("Usage: python %s GITHUB_API_KEY" % sys.argv[0])
	exit(0)

GITHUB_API_KEY = sys.argv[1]
headers = {"Authorization": "Bearer " + GITHUB_API_KEY, "content-type": "application/json"}

query = """
{
  search(query: "repo:Andati/BriskPesa is:pr is:merged merged:>2019-12-09", type: ISSUE, last: 100) {
    edges {
      node {
        ... on PullRequest {
          url
          title
          bodyText
          createdAt
          mergedAt
        }
      }
    }
  }
}
"""

def cleanBodyText(body):
	res = "";
	for i in range(len(body)):
	  if(body[i]=='\n'):
		if len(res)>1 and res[len(res)-1] != '\n':
		  res += body[i]
	  else:
		res += body[i]
	return res

response = requests.post(GraphQL_URL, data=json.dumps({'query':query}), headers=headers)
if response.status_code == 200:
	json_response = response.json()
	edges = json_response['data']['search']['edges']
	for edge in edges:
		print cleanBodyText(edge['node']['bodyText'])
else:
	print "Error code returned " + str(response.status_code)


