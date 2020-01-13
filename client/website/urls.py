from django.urls import path
from website.views import Index, SearchResult, MainPage

urlpatterns = [
    path('', MainPage.as_view()),
    path('searchResult/', SearchResult.as_view()),
    path('index/', Index.as_view()),
]