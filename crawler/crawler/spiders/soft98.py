from scrapy import Spider
from scrapy import Request
import re


class Soft98Spider(Spider):
    name = 'soft98'

    start_urls = ['https://soft98.ir']
    custom_settings = {
        'CONCURRENT_REQUESTS': 64
    }

    def parse(self, response):
        # next_url = response.xpath('//ul[contains(@class, "pagination")]/li[last()]/a/@href').get()
        last_page_number = int(response.xpath('//ul[contains(@class, "pagination")]/li')[-2].xpath('a/text()').get())
        for i in range(1, last_page_number):
            yield Request(
                url=f'https://soft98.ir/page/{i}/',
                callback=self.parse_pages
            )

    def parse_pages(self, response):
        articles = response.xpath('//main[@id="main"]/article')
        links = articles.xpath('header/h2/a/@href').getall()
        titles = articles.xpath('header/h2/a/@title').getall()
        body = articles.xpath('div/text()').getall()

        for item in zip(titles, links, body):
            yield {
                'title': item[0],
                'link': item[1],
                'body': item[2],
                'siteName': 'soft98'
            }