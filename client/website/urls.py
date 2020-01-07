from django.urls import path
from website.views import Search, Index

urlpatterns = [
    path('search/', Search.as_view()),
    path('index/', Index.as_view())
]