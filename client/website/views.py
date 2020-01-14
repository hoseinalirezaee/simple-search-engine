from django.shortcuts import render
from django.views import View
from django.http import HttpResponse, JsonResponse
from subprocess import Popen, run,PIPE
from client import settings
from pymongo import MongoClient
from bson import ObjectId
from website.models import Searcher, Indexer
from django.views.generic import ListView
from django.http import HttpRequest, HttpResponseRedirect


class Index(View):
    def get(self, request):
        return render(request, 'index.html')

    def post(self, request):
        Indexer.index()
        context = {
            'done': 'Indexing Done.'
        }
        return render(request, 'index.html', context=context)

class StopWord(View):

    def post(self, request):
        Indexer.create_stopwords()
        context = {
            'done': 'Creating stop words done.'
        }
        return render(request, 'index.html', context=context)


class MainPage(View):

    def get(self, request: HttpRequest):
        return render(request, 'search.html')


class SearchResult(ListView):
    paginate_by = 10
    context_object_name = 'docs'
    template_name = 'search_result.html'

    def get_queryset(self):
        request: HttpRequest = self.request
        query = request.GET.get('q', None)
        self.extra_context = {'query': query}
        docs = Searcher.search(query)
        for doc in docs:
            doc.body = doc.body[:130]
        return docs
