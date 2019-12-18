import requests
import sys
import json

GraphQL_URL = "https://api.github.com/graphql"

if len(sys.argv) == 1:
	print "pass your GitHub key to this script"
	exit(0)

headers = {"Authorization": "Bearer " + sys.argv[1], 'content-type': 'application/json'}

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

response = requests.post(GraphQL_URL, data=json.dumps({'query':query}), headers=headers)
if response.status_code == 200:
	json_response = response.json()
	#print json_response['data']
	edges = json_response['data']['search']['edges']
	for edge in edges:
		#print edge['node']['title']
		print edge['node']['bodyText']
else:
	print "Error code returned " + str(response.status_code)

