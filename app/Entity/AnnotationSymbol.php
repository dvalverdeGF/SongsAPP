<?php

namespace App\Entity;

use App\Repository\AnnotationSymbolRepository;
use Doctrine\Common\Collections\ArrayCollection;
use Doctrine\Common\Collections\Collection;
use Doctrine\ORM\Mapping as ORM;

#[ORM\Entity(repositoryClass: AnnotationSymbolRepository::class)]
class AnnotationSymbol
{
    #[ORM\Id]
    #[ORM\GeneratedValue]
    #[ORM\Column]
    private ?int $id = null;

    // Código abreviado o símbolo gráfico, ej: "𝆏" para respiración, "p" para piano
    #[ORM\Column(length: 20, unique: true)]
    private string $code;

    // Nombre descriptivo del símbolo
    #[ORM\Column(length: 100)]
    private string $name;

    #[ORM\OneToMany(mappedBy: 'symbol', targetEntity: Annotation::class)]
    private Collection $annotations;

    public function __construct()
    {
        $this->annotations = new ArrayCollection();
    }

    public function getId(): ?int
    {
        return $this->id;
    }

    public function getCode(): ?string
    {
        return $this->code;
    }

    public function setCode(string $code): static
    {
        $this->code = $code;

        return $this;
    }

    public function getName(): ?string
    {
        return $this->name;
    }

    public function setName(string $name): static
    {
        $this->name = $name;

        return $this;
    }

    /**
     * @return Collection<int, Annotation>
     */
    public function getAnnotations(): Collection
    {
        return $this->annotations;
    }

    public function addAnnotation(Annotation $annotation): static
    {
        if (!$this->annotations->contains($annotation)) {
            $this->annotations->add($annotation);
            $annotation->setSymbol($this);
        }

        return $this;
    }

    public function removeAnnotation(Annotation $annotation): static
    {
        if ($this->annotations->removeElement($annotation)) {
            // set the owning side to null (unless already changed)
            if ($annotation->getSymbol() === $this) {
                $annotation->setSymbol(null);
            }
        }

        return $this;
    }
}
