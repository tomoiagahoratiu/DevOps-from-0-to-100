FROM node:14-alpine
WORKDIR /opt/work
COPY ./package.json ./
COPY ./package-lock.json ./
RUN npm install
COPY . ./
CMD ["npm", "start"]