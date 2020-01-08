from django.shortcuts import render
from django.views import View
from django.http import HttpResponse, JsonResponse
from subprocess import Popen, run,PIPE
from client import settings
from pymongo import MongoClient
from bson import ObjectId
from website.models import Searcher, Indexer


class Index(View):
    def get(self, request):
        return render(request, 'index.html')

    def post(self, request):
        Indexer.index()
        return HttpResponse('Done'.encode('utf-8'))


class Search(View):

    def get(self, request):
        return render(request, 'search.html')

    def post(self, request):
        query = request.POST.get('query', None)
        if query:
            docs = Searcher.search(query)
            return render(request, 'search_result.html', context={'docs': docs})
        return HttpResponse('None'.encode('utf-8'))

    def getResutlFromDB(self, ids):
        client = MongoClient('mongodb://hosein:123456@localhost:27017/admin')
        cursor = client.index_test.data.find({'_id': ObjectId(ids[0])})
        return [item for item in cursor]