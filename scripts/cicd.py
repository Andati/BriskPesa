import requests
import sys
import json
from datetime import datetime, timedelta

GraphQL_URL = "https://api.github.com/graphql"
PERIOD = 150 #days

if len(sys.argv) < 3:
	print("Usage: python %s GITHUB_API_KEY REPO_SLUG" % sys.argv[0])
	exit(0)

GITHUB_API_KEY = sys.argv[1]
REPO_SLUG = sys.argv[2]
headers = {"Authorization": "Bearer " + GITHUB_API_KEY, "content-type": "application/json"}

date_since = (datetime.today() - timedelta(days=PERIOD)).strftime("%Y-%m-%d")

query = """
	{
	  search(query: "repo:%s is:pr is:merged merged:>%s", type: ISSUE, last: 100) {
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
	""" % (REPO_SLUG, date_since)

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
	if len(edges) == 0:
		print "No PR found given the search params: " + REPO_SLUG
	for edge in edges:
		print cleanBodyText(edge['node']['bodyText'])
else:
	print "Error code returned " + str(response.status_code)

