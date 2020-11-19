{{/* vim: set filetype=mustache: */}}
{{/*
Expand the name of the chart.
*/}}
{{- define "homework.name" -}}
{{- default .Chart.Name .Values.nameOverride | trunc 63 | trimSuffix "-" }}
{{- end }}

{{/*
Create a default fully qualified app name.
We truncate at 63 chars because some Kubernetes name fields are limited to this (by the DNS naming spec).
If release name contains chart name it will be used as a full name.
*/}}
{{- define "homework.fullname" -}}
{{- if .Values.fullnameOverride }}
{{- .Values.fullnameOverride | trunc 63 | trimSuffix "-" }}
{{- else }}
{{- $name := default .Chart.Name .Values.nameOverride }}
{{- if contains $name .Release.Name }}
{{- .Release.Name | trunc 63 | trimSuffix "-" }}
{{- else }}
{{- printf "%s-%s" .Release.Name $name | trunc 63 | trimSuffix "-" }}
{{- end }}
{{- end }}
{{- end }}

{{/*
Create chart name and version as used by the chart label.
*/}}
{{- define "homework.chart" -}}
{{- printf "%s-%s" .Chart.Name .Chart.Version | replace "+" "_" | trunc 63 | trimSuffix "-" }}
{{- end }}

{{/*
Common labels
*/}}
{{- define "homework.labels" -}}
helm.sh/chart: {{ include "homework.chart" . }}
{{ include "homework.selectorLabels" . }}
{{- if .Chart.AppVersion }}
app.kubernetes.io/version: {{ .Chart.AppVersion | quote }}
{{- end }}
app.kubernetes.io/managed-by: {{ .Release.Service }}
{{- end }}

{{/*
Selector labels
*/}}
{{- define "homework.selectorLabels" -}}
app.kubernetes.io/name: {{ include "homework.name" . }}
app.kubernetes.io/instance: {{ .Release.Name }}
{{- end }}

{{/*
Create the name of the service account to use
*/}}
{{- define "homework.serviceAccountName" -}}
{{- if .Values.serviceAccount.create }}
{{- default (include "homework.fullname" .) .Values.serviceAccount.name }}
{{- else }}
{{- default "default" .Values.serviceAccount.name }}
{{- end }}
{{- end }}

Create a default fully qualified app name.
We truncate at 63 chars because some Kubernetes name fields are limited to this (by the DNS naming spec).
*/}}
{{- define "authdb.fullname" -}}
{{- printf "%s-%s" .Release.Name "authdb" | trunc 63 | trimSuffix "-" -}}
{{- end -}}

Create a default fully qualified app name.
We truncate at 63 chars because some Kubernetes name fields are limited to this (by the DNS naming spec).
*/}}
{{- define "authredis.fullname" -}}
{{- printf "%s-%s" .Release.Name "authredis" | trunc 63 | trimSuffix "-" -}}
{{- end -}}

Create a default fully qualified app name.
We truncate at 63 chars because some Kubernetes name fields are limited to this (by the DNS naming spec).
*/}}
{{- define "billingdb.fullname" -}}
{{- printf "%s-%s" .Release.Name "billingdb" | trunc 63 | trimSuffix "-" -}}
{{- end -}}

Create a default fully qualified app name.
We truncate at 63 chars because some Kubernetes name fields are limited to this (by the DNS naming spec).
*/}}
{{- define "notificationsdb.fullname" -}}
{{- printf "%s-%s" .Release.Name "notificationsdb" | trunc 63 | trimSuffix "-" -}}
{{- end -}}

Create a default fully qualified app name.
We truncate at 63 chars because some Kubernetes name fields are limited to this (by the DNS naming spec).
*/}}
{{- define "ordersdb.fullname" -}}
{{- printf "%s-%s" .Release.Name "ordersdb" | trunc 63 | trimSuffix "-" -}}
{{- end -}}

Create a default fully qualified app name.
We truncate at 63 chars because some Kubernetes name fields are limited to this (by the DNS naming spec).
*/}}
{{- define "warehousedb.fullname" -}}
{{- printf "%s-%s" .Release.Name "warehousedb" | trunc 63 | trimSuffix "-" -}}
{{- end -}}

Create a default fully qualified app name.
We truncate at 63 chars because some Kubernetes name fields are limited to this (by the DNS naming spec).
*/}}
{{- define "prometheus-operator.fullname" -}}
{{- printf "%s-%s" .Release.Name "prometheus-operator" | trunc 63 | trimSuffix "-" -}}
{{- end -}}

Create a default fully qualified app name.
We truncate at 63 chars because some Kubernetes name fields are limited to this (by the DNS naming spec).
*/}}
{{- define "prometheus-postgres-exporter.fullname" -}}
{{- printf "%s-%s" .Release.Name "prometheus-postgres-exporter" | trunc 63 | trimSuffix "-" -}}
{{- end -}}