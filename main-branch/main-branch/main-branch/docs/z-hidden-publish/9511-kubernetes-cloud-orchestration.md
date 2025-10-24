# kae3g 9511: Kubernetes - Container Orchestration at Scale

**Phase 1: Foundations & Philosophy** | **Week 2** | **Reading Time: 12 minutes**

---

## What You'll Learn

- What Kubernetes is and why it dominates enterprise
- Container orchestration fundamentals
- Pods, Deployments, Services (core concepts)
- How Kubernetes embodies Unix philosophy (Essay 9510!)
- Running Kubernetes locally (minikube, k3s)
- When to use Kubernetes vs. simpler alternatives
- **Fast track to cloud orchestration mastery!**

---

## Prerequisites

- **[9500: What Is a Computer?](/12025-10/9500-what-is-a-computer)** - Hardware foundations
- **[9501: What Is Compute?](/12025-10/9501-what-is-compute)** - Cloud, distributed systems
- **[9510: Unix Philosophy](/12025-10/9510-unix-philosophy-primer)** - Composition principles (critical!)

---

## What Is Kubernetes?

**Kubernetes** (k8s) is a **container orchestration platform**.

**What that means**:
- You have **containers** (isolated apps, like Docker - Essay 9570!)
- You have **many machines** (cluster of nodes)
- **Kubernetes** decides where containers run, restarts them if they crash, routes traffic between them

**Born at Google** (2014), now **industry standard** for cloud deployments.

**Think of it as**: Unix process management (Essay 9570), but for **distributed systems** at cloud scale!

---

## Why Kubernetes Exists

**The problem**: Modern apps are distributed systems.

**Example web app**:
- 10 web servers (HTTP)
- 5 API servers (business logic)
- 3 database replicas
- 2 cache servers (Redis)
- 1 message queue (Kafka)

**Without orchestration**:
- Manual SSH to each machine
- Start/stop services by hand
- No automatic recovery
- Scaling = manual work

**With Kubernetes**:
- Declare desired state (YAML)
- Kubernetes makes it happen (automatically)
- Self-healing (crashed pod → new pod)
- Auto-scaling (traffic → more pods)
- Load balancing (built-in)

---

## Core Concepts

### 1. Pods (Smallest Unit)

```yaml
apiVersion: v1
kind: Pod
metadata:
  name: my-app
spec:
  containers:
  - name: web
    image: nginx:latest
    ports:
    - containerPort: 80
```

**Pod** = one or more containers sharing:
- Network (same IP)
- Storage
- Lifecycle (start/stop together)

**Typically**: One container per pod.

---

### 2. Deployments (Manage Replicas)

```yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: web-app
spec:
  replicas: 3  # Run 3 copies!
  selector:
    matchLabels:
      app: web
  template:
    metadata:
      labels:
        app: web
    spec:
      containers:
      - name: nginx
        image: nginx:1.21
```

**Deployment ensures**:
- 3 replicas always running
- Rolling updates (gradual replacement)
- Rollback (if new version breaks)

**This is declarative** (like Nix!): You declare "I want 3", Kubernetes ensures 3 exist.

---

### 3. Services (Stable Networking)

```yaml
apiVersion: v1
kind: Service
metadata:
  name: web-service
spec:
  selector:
    app: web
  ports:
  - port: 80
    targetPort: 80
  type: LoadBalancer
```

**Problem**: Pods have dynamic IPs (come and go).

**Solution**: Service provides stable endpoint:
- DNS name
- Load balances across replicas
- Survives pod restarts

---

## Kubernetes Architecture (Simplified)

```
Control Plane (Brain)
├── API Server (central interface)
├── Scheduler (assigns pods to nodes)
├── Controller Manager (reconciliation)
└── etcd (state storage)
        │
    ────┴────────────────
    │         │         │
  Node 1   Node 2   Node 3
  (Pods)   (Pods)   (Pods)
```

**Control Plane**: Makes decisions  
**Nodes**: Run workloads

---

## Unix Philosophy in Kubernetes

**Kubernetes embodies Unix principles** (Essay 9510):

### 1. Do One Thing Well

**Each component specialized**:
- **Scheduler**: Decides placement (one job)
- **Controller**: Reconciles state (one job)
- **kubelet**: Runs containers (one job)
- **Service**: Provides networking (one job)

**Not monolithic**. Each part replaceable.

### 2. Composition

```yaml
# Compose simple resources
Pod + Service + Deployment = Complete app
```

**Like Unix pipes**: Simple tools → complex workflows.

### 3. Declarative

**Unix**:
```bash
ls | grep ".txt" | wc -l  # "Count .txt files" (what, not how)
```

**Kubernetes**:
```yaml
replicas: 3  # "I want 3" (what, not how)
```

Kubernetes figures out **HOW** (like Nix!).

---

## Running Kubernetes Locally

### Option 1: Minikube

```bash
# Install (macOS)
brew install minikube

# Start
minikube start

# Deploy nginx
kubectl create deployment nginx --image=nginx
kubectl expose deployment nginx --port=80 --type=NodePort

# Access
minikube service nginx
```

**Pros**: Full k8s API  
**Cons**: Resource-heavy (VM)

### Option 2: K3s (Lightweight!)

```bash
# Install (100MB binary)
curl -sfL https://get.k3s.io | sh -

# Use
export KUBECONFIG=/etc/rancher/k3s/k3s.yaml
kubectl get nodes

# Deploy
kubectl create deployment nginx --image=nginx
```

**Pros**: Fast, lightweight, production-ready  
**Cons**: Slightly different from full k8s

---

## When to Use Kubernetes

**Use Kubernetes when**:
- **Scale**: 100+ services, 1000+ containers
- **Multi-tenancy**: Multiple teams, shared infrastructure
- **High availability**: 99.99%+ uptime needed
- **Auto-scaling**: Traffic varies dramatically
- **Team coordination**: Many developers, CI/CD pipelines

**Examples**:
- SaaS products (Stripe, Shopify)
- E-commerce platforms
- Social media backends
- Enterprise applications

**Don't use Kubernetes when**:
- Small projects (1-3 services)
- Personal apps (overkill!)
- Learning (too complex to start)
- Simple deployments (Docker Compose sufficient)

**Rule of thumb**: Start simple. Add Kubernetes when you outgrow simpler tools.

---

## Try This

### Exercise: Run K3s Locally

```bash
# Install kubectl
brew install kubectl

# Deploy app
kubectl create deployment hello --image=rancher/hello-world

# Expose
kubectl expose deployment hello --port=80 --type=NodePort

# Get service port
kubectl get services

# Visit http://localhost:<PORT>
```

**Observe**:
- Declarative deployment
- Automatic pod creation
- Self-healing (kill pod, watch restart!)

---

## Summary

**Kubernetes is**:
- **Container orchestration** (manage 1000s of containers)
- **Declarative** (YAML configs)
- **Self-healing** (automatic recovery)
- **Scalable** (auto-scaling, load balancing)

**Core concepts**:
- **Pods**: Smallest unit (1+ containers)
- **Deployments**: Manage replicas
- **Services**: Stable networking

**Unix Philosophy in Kubernetes**:
- Each component does one thing well
- Compose resources (like pipes)
- Declarative (what, not how)

**When to use**:
- Scale (100+ services)
- Teams (coordination needed)
- High availability (99.99%+)

**When NOT to use**:
- Small projects (overkill!)
- Learning (too complex)
- Personal apps (simpler alternatives exist!)

**Modern applications**:
- **Kubernetes**: Enterprise scale (you are here!)
- **Framework laptops**: Personal sovereignty (Essay 9513!)
- **Both use Unix philosophy**: Modularity, composition, simplicity

**In the Valley**:
- We understand **scale** (Kubernetes handles it!)
- We choose **consciously** (k8s for teams, simpler tools for solo)
- We apply **Unix principles** everywhere (composition, modularity)

**Plant lens**: "Kubernetes is large-scale agricultural coordination (managing thousands of crop rows). Simple deployments are home gardens (tend directly). Choose the right scale for your needs."

---

**Next**: Continue to **functional programming** (Essay 9520), or explore the deep dives!

**Optional Deep Dives** (can skip or read later):
- **[9512: Unix Philosophy Deep Dive](/12025-10/9512-unix-philosophy-deep-dive)** - Verified Unix, seL4, Nock specifications!
- **[9513: Personal Sovereignty Stack](/12025-10/9513-personal-sovereignty-framework-stack)** - Framework laptops, RISC-V, complete hardware/software control!

---

**Navigation**:  
← Previous: [9510 (unix philosophy primer)](/12025-10/9510-unix-philosophy-primer) | **Phase 1 Index** | Next: [9512 (unix philosophy deep dive)](/12025-10/9512-unix-philosophy-deep-dive)

**Optional Deep Dives**: [9512 (Unix Deep)](/12025-10/9512-unix-philosophy-deep-dive) | [9513 (Sovereignty Deep)](/12025-10/9513-personal-sovereignty-framework-stack)

**Metadata**:
- **Phase**: 1 (Foundations)
- **Week**: 2  
- **Prerequisites**: 9500, 9501, 9510
- **Concepts**: Kubernetes, container orchestration, pods, deployments, services, declarative infrastructure
- **Next**: Functional programming (9520), or optional deep dives (9512, 9513)
- **Reading Time**: 12 minutes (focused on k8s essentials!)




---

<div style="text-align: center; opacity: 0.6; font-size: 0.85em; margin-top: 3em; padding-top: 1em; border-top: 1px solid rgba(139, 116, 94, 0.2);">

**Copyright © 2025 [kae3g](https://codeberg.org/kae3g/12025-10/)** | Dual-licensed under [Apache-2.0](https://www.apache.org/licenses/LICENSE-2.0) / [MIT](https://opensource.org/licenses/MIT)  
Competitive technology in service of clarity and beauty

</div>


*[View Hidden Docs Index](/12025-10/hidden-docs-index.html)* | *[Return to Main Index](/12025-10/)*