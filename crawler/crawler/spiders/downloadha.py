from scrapy import Spider
from scrapy import Request
import re


class MyDict(dict):

    def __repr__(self):
        return self['title']


class DownloadhaSpider(Spider):
    name = 'downloadha'

    start_urls = ['https://downloadha.com']
    custom_settings = {
        'CONCURRENT_REQUESTS': 64
    }

    def parse(self, response):
        # next_url = response.xpath('//ul[contains(@class, "pagination")]/li[last()]/a/@href').get()
        last_page_url = response.xpath('//ul[contains(@class, "pagination")]/li')[-1].xpath('a/@href')
        last_page_number = int(last_page_url.get().split('/')[-2])
        for i in range(1, last_page_number):
            yield Request(
                url=f'https://www.downloadha.com/page/{i}/',
                callback=self.parse_pages
            )

    def parse_pages(self, response):
        articles = response.xpath('//main[@id="main"]/article')
        links = articles.xpath('header/h2/a/@href').getall()
        titles = articles.xpath('header/h2/a/text()').getall()
        body_html = articles.xpath('div')

        for item in zip(titles, links, body_html):
            body_list = item[2].xpath('p[contains(@style, "justify")]/text()').getall()
            body = ''
            for t in body_list:
                body += (t + '\n')

            yield MyDict({
                'title': item[0],
                'link': item[1],
                'body': body,
                'siteName': 'downlaodha'
            })