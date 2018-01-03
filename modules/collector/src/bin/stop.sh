#!/bin/bash
ps -ef|grep kepler-collector.jar|grep -v grep|awk '{print $2}'|xargs kill
