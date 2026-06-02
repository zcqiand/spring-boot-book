# 部署所有资源
kubectl apply -f configmap.yaml
kubectl apply -f deployment.yaml
kubectl apply -f service.yaml
kubectl apply -f ingress.yaml

# 查看部署状态
kubectl get pods -l app=spring-boot-app
kubectl get services
kubectl get ingress

# 查看Pod日志
kubectl logs -f deployment/spring-boot-app

# 滚动更新
kubectl set image deployment/spring-boot-app spring-boot-app=myregistry/spring-boot-app:1.1.0
kubectl rollout status deployment/spring-boot-app

# 回滚
kubectl rollout undo deployment/spring-boot-app

# 清理资源
kubectl delete -f ingress.yaml -f service.yaml -f deployment.yaml -f configmap.yaml