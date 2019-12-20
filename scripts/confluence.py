import json
import requests
import sys
from requests.auth import HTTPBasicAuth
from datetime import datetime

if len(sys.argv) < 4:
	print("Usage: python %s ATLASSIAN_EMAIL ATLASSIAN_TOKEN GITHUB_API_KEY" % sys.argv[0])
	exit(0)

BASE_URL = "https://smartregister.atlassian.net/wiki/rest/api/content"
GraphQL_URL = "https://api.github.com/graphql"
ATLASSIAN_EMAIL = sys.argv[1]
ATLASSIAN_TOKEN = sys.argv[2]
GITHUB_API_KEY = sys.argv[3]

BASIC_AUTH = HTTPBasicAuth(ATLASSIAN_EMAIL, ATLASSIAN_TOKEN)

def get_page_json(page_id, expand = False):
	if expand:
		suffix = "?expand=" + expand
	else:
		suffix = ""

	url=BASE_URL + "/" + page_id + suffix
	response = requests.get(url, auth=BASIC_AUTH)
	if response.status_code == 200:
		json_body = json.loads(response.text)
		return json_body
	else:
		print "Error code returned " + str(response.status_code)

def get_page_info(page_id):
	url = '{base}/{page_id}'.format(
        base = BASE_URL,
        page_id = page_id)
	response = requests.get(url, auth = BASIC_AUTH)
	if response.status_code == 200:
		return json.loads(response.text)
	else:
		print "Error code returned " + str(response.status_code)

def update_page(page_id, html):
	info = get_page_info(page_id)
	version = int(info['version']['number']) + 1

	data = {
        'id' : str(page_id),
        'type' : 'page',
        'title' : info['title'],
        'version' : {'number' : version},
        'body' : {
            'storage' :
            {
                'representation' : 'storage',
                'value' : str(html),
            }
        }
     }

	data = json.dumps(data)
	url = '{base}/{page_id}'.format(base = BASE_URL, page_id = page_id)

	response = requests.put(
		url,
		data = data,
		headers = { 'Content-Type' : 'application/json' },
		auth = BASIC_AUTH
	)
	if response.status_code == 200:
		return json.loads(response.text)
	else:
		print "Error code returned " + str(response.status_code)

def insert_row_to_page(current_content, new_html):
	# find last occurrence of </tbody>
	body = current_content['body']['storage']['value']
	k = body.rfind("</tbody>")
	return body[:k] + new_html + body[k:]


def get_release_details():
	headers = {"Authorization": "Bearer " + GITHUB_API_KEY, "content-type": "application/json"}
	query = """
	{
	  repository(owner: "Andati", name: "BriskPesa") {
		releases(last: 1) {
		  edges{
		    node{
		      tagName
		      publishedAt
		      description
		      releaseAssets(first: 1) {
		        nodes {
		          downloadUrl
		        }
		      }
		    }
		  }
		}
	  }
	}
	"""
	response = requests.post(GraphQL_URL, data=json.dumps({'query':query}), headers=headers)
	if response.status_code == 200:
		json_response = response.json()
		tag_name = json_response['data']['repository']['releases']['edges'][0]['node']['tagName']
		published_at = json_response['data']['repository']['releases']['edges'][0]['node']['publishedAt']
		description = json_response['data']['repository']['releases']['edges'][0]['node']['description']
		download_url = json_response['data']['repository']['releases']['edges'][0]['node']['releaseAssets']['nodes'][0]['downloadUrl']
		return {"published_at":published_at, "description":description, "download_url":download_url, "tag_name":tag_name}
	else:
		print "Error code returned " + str(response.status_code)

def suffix(day):
    return 'th' if 11<=day<=13 else {1:'st',2:'nd',3:'rd'}.get(day%10, 'th')

def custom_strftime(format, t):
	return t.strftime(format).replace('{S}', str(t.day) + suffix(t.day))
    
current_content = get_page_json("1245380625", "body.storage")
#print current_content

release_details = get_release_details()
#print release_details

published_at = datetime.strptime(release_details["published_at"], "%Y-%m-%dT%H:%M:%SZ")

new_html = '<tr><td><p><a href="'+ release_details["download_url"] +'">'+ release_details["tag_name"] +'</a></p></td><td><p>'+ custom_strftime('%b {S}, %Y', published_at) +'</p></td><td><p>'+ release_details["description"] +'</p></td></tr>'
print new_html

res = update_page("1245380625", insert_row_to_page(current_content, new_html))
#print(res)

