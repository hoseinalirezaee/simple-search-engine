from client.settings import INDEX_PATH, SEARCHER_PATH, INDEXER_PATH, DATABASES
from subprocess import run, PIPE
from bson import ObjectId
from pymongo import MongoClient

mongo_info = DATABASES['mongodb']


class Searcher(object):

    @staticmethod
    def search(query):
        process_return = run(
            ['java', '-jar', SEARCHER_PATH, INDEX_PATH, query],
            stdout=PIPE
        )

        result_string = process_return.stdout.decode('utf-8')
        docs_id = result_string.split('\n')[:-1]
        docs = DatabaseConnect.get(docs_id)
        return docs


class Indexer(object):

    @staticmethod
    def index():
        process_return = run(
            ['java', '-jar', INDEXER_PATH, INDEX_PATH, 'mongodb',
             mongo_info['HOSTNAME'], str(mongo_info['PORT']), mongo_info['USERNAME'], mongo_info['PASSWORD'],
             mongo_info['AUTH_DB'], mongo_info['DATA_DB'], mongo_info['DATA_COLLECTION']],
        )


class DatabaseConnect(object):

    @staticmethod
    def get(ids):
        client = MongoClient('mongodb://{0}:{1}@{2}:{3}/{4}'.format(
            mongo_info['USERNAME'], mongo_info['PASSWORD'],
            mongo_info['HOSTNAME'], mongo_info['PORT'], mongo_info['AUTH_DB']
        ))
        database = client.get_database(mongo_info['DATA_DB'])
        collection = database.get_collection(mongo_info['DATA_COLLECTION'])

        objectIds = [ObjectId(id) for id in ids]
        docs = collection.find({'_id': {'$in': objectIds}})
        f_docs = []
        for doc in docs:
            del doc['_id']
            d = Doc()
            d.title = doc['title']
            d.body = doc['body']
            d.link = doc['link']
            d.site_name = doc['siteName']
            f_docs.append(d)
        return f_docs


class Doc:

    title = str()
    body = str()
    link = str()
    site_name = str()